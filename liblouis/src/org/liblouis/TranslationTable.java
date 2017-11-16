package org.liblouis;

import java.io.File;
import java.util.Arrays;

import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class TranslationTable extends Translator {
  private final TableFile forwardTable;
  private final TableFile backwardTable;

  TranslationTable (String forwardName, String backwardName) {
    super();

    forwardTable = new TableFile(forwardName);
    backwardTable = backwardName.equals(forwardName)?
                    forwardTable: new TableFile(backwardName);
  }

  TranslationTable (String name) {
    this(name, name);
  }

  public final TableFile getForwardTableFile () {
    return forwardTable;
  }

  public final TableFile getBackwardTableFile () {
    return backwardTable;
  }

  public final TableFile getTableFile () {
    return getForwardTableFile();
  }

  public final String getTableName () {
    return getTableFile().getTableName();
  }

  public final String getFileName () {
    return getTableFile().getFileName();
  }

  public final File getFileObject () {
    return getTableFile().getFileObject();
  }

  public final short getEmphasisBit (String name) {
    return getTableFile().getEmphasisBit(name);
  }

  private final static short TYPE_FORM_BOLD = Emphasis.getBoldBit();
  private final static short TYPE_FORM_ITALIC = Emphasis.getItalicBit();
  private final static short TYPE_FORM_UNDERLINE = Emphasis.getUnderlineBit();

  private static short[] createTypeForm (int length) {
    short[] typeForm = new short[length];
    Arrays.fill(typeForm, (short)0);
    return typeForm;
  }

  private static short[] createTypeForm (int length, CharSequence text) {
    short[] typeForm = null;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        for (Object span : spans) {
          short flags = 0;

          if (span instanceof UnderlineSpan) {
            flags |= TYPE_FORM_UNDERLINE;
          } else if (span instanceof StyleSpan) {
            switch (((StyleSpan)span).getStyle()) {
              case Typeface.BOLD:
                flags |= TYPE_FORM_BOLD;
                break;

              case Typeface.ITALIC:
                flags |= TYPE_FORM_ITALIC;
                break;

              case Typeface.BOLD_ITALIC:
                flags |= TYPE_FORM_BOLD;
                flags |= TYPE_FORM_ITALIC;
                break;
            }
          }

          if (flags != 0) {
            if (typeForm == null) typeForm = createTypeForm(length);

            int start = spanned.getSpanStart(span);
            int end = spanned.getSpanEnd(span);

            for (int index=start; index<end; index+=1) {
              typeForm[index] |= flags;
            }
          }
        }
      }
    }

    return typeForm;
  }

  private native boolean translate (
    String tableName,
    String inputBuffer, char[] outputBuffer, short[] typeForm,
    int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate
  );

  @Override
  public final boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  ) {
    final TableFile tableFile =
      backTranslate?
      getBackwardTableFile():
      getForwardTableFile();

    final int inputLength = inputBuffer.length();
    final int outputLength = outputBuffer.length;

    final int typeFormLength = Math.max(inputLength, outputLength);
    final short[] typeForm =
      !includeHighlighting? null:
      backTranslate? null:
      createTypeForm(typeFormLength, inputBuffer);

    synchronized (Louis.NATIVE_LOCK) {
      boolean translated = translate(
        tableFile.getFileName(), inputBuffer.toString(), outputBuffer,
        typeForm, outputOffsets, inputOffsets, resultValues, backTranslate
      );

      if (!translated) return false;
    }

    if (backTranslate) {
      {
        int outStart = 0;

        while (true) {
          int inputOffset = resultValues[RVI_INPUT_LENGTH];
          if (inputOffset == inputLength) break;

          int outputOffset = outputOffsets[inputOffset];
          if (outputOffset < outStart) break;
          if (outputOffset > outputLength) break;

          outStart = outputOffset;
          resultValues[RVI_INPUT_LENGTH] = inputOffset + 1;
        }
      }

    FIX_END:
      if (resultValues[RVI_INPUT_LENGTH] == inputLength) {
        int outLength = resultValues[RVI_OUTPUT_LENGTH];

        while (true) {
          int inputOffset = resultValues[RVI_INPUT_LENGTH];
          if (inputOffset == 0) break;

          if (outputOffsets[inputOffset -= 1] != outLength) break;
          resultValues[RVI_INPUT_LENGTH] = inputOffset;
        }

        while (true) {
          int inputOffset = resultValues[RVI_INPUT_LENGTH];
          if (inputOffset == inputLength) break;

          int outputOffset = resultValues[RVI_OUTPUT_LENGTH];
          if (outputOffset == outputLength) break FIX_END;

          outputBuffer[outputOffset] = inputBuffer.charAt(inputOffset);
          outputOffsets[inputOffset] = outputOffset;
          inputOffsets[outputOffset] = inputOffset;

          resultValues[RVI_INPUT_LENGTH] = inputOffset + 1;
          resultValues[RVI_OUTPUT_LENGTH] = outputOffset + 1;
        }
      }
    }

    return true;
  }
}
