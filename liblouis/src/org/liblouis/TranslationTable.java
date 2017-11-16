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

  public final boolean translate (
    String inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  ) {
    final TableFile tableFile =
      backTranslate?
      getBackwardTableFile():
      getForwardTableFile();
    final String fileName = tableFile.getFileName();

    final int typeFormLength = Math.max(
      inputBuffer.length(), outputBuffer.length
    );

    final short[] typeForm = !includeHighlighting? null:
                             backTranslate? null:
                             createTypeForm(typeFormLength, inputBuffer);

    synchronized (Louis.NATIVE_LOCK) {
      return translate(
        fileName,
        inputBuffer, outputBuffer, typeForm,
        outputOffsets, inputOffsets, resultValues,
        backTranslate
      );
    }
  }
}
