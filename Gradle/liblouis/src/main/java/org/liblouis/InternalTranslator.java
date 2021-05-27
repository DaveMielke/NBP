package org.liblouis;

import java.util.Arrays;

import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class InternalTranslator extends Translator {
  private final InternalTable forwardTable;
  private final InternalTable backwardTable;

  InternalTranslator (String forwardTableList, String backwardTableList) {
    super();

    forwardTable = new InternalTable(forwardTableList);
    backwardTable = (backwardTableList == null)? forwardTable:
                    backwardTableList.equals(forwardTableList)? forwardTable:
                    new InternalTable(backwardTableList);
  }

  InternalTranslator (String tableList) {
    this(tableList, tableList);
  }

  public final InternalTable getForwardTable () {
    return forwardTable;
  }

  public final InternalTable getBackwardTable () {
    return backwardTable;
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

  private native static boolean translate (
    String tableList, String inputBuffer, char[] outputBuffer,
    short[] typeForm, int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate, boolean noContractions
  );

  @Override
  public final boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate,
    boolean includeHighlighting, boolean noContractions
  ) {
    final InternalTable table = backTranslate? getBackwardTable(): getForwardTable();

    final int inputLength = inputBuffer.length();
    final int outputLength = outputBuffer.length;

    final int typeFormLength = Math.max(inputLength, outputLength);
    final short[] typeForm =
      !includeHighlighting? null:
      backTranslate? null:
      createTypeForm(typeFormLength, inputBuffer);

    synchronized (Louis.NATIVE_LOCK) {
      boolean translated = translate(
        table.getList(), inputBuffer.toString(), outputBuffer,
        typeForm, outputOffsets, inputOffsets,
        resultValues, backTranslate, noContractions
      );

      if (!translated) return false;
    }

    if (backTranslate) {
      {
        int outStart = 0;

        while (true) {
          final int inOffset = resultValues[RVI_INPUT_LENGTH];
          if (inOffset == inputLength) break;

          final int outOffset = outputOffsets[inOffset];
          if (outOffset < outStart) break;
          if (outOffset > outputLength) break;

          outStart = outOffset;
          resultValues[RVI_INPUT_LENGTH] = inOffset + 1;
        }
      }

      if (resultValues[RVI_INPUT_LENGTH] == inputLength) {
        final int outLength = resultValues[RVI_OUTPUT_LENGTH];

        while (true) {
          int inOffset = resultValues[RVI_INPUT_LENGTH];
          if (inOffset == 0) break;

          if (outputOffsets[inOffset -= 1] != outLength) break;
          resultValues[RVI_INPUT_LENGTH] = inOffset;
        }
      }
    }

    return true;
  }
}
