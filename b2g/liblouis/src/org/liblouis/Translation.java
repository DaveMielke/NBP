package org.liblouis;

import java.util.Arrays;

import android.util.Log;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class Translation {
  private final static String LOG_TAG = Translation.class.getName();

  private native boolean translate (
    String tableName,
    String inputBuffer, char[] outputBuffer, byte[] typeForm,
    int[] outputOffsets, int[] inputOffsets,
    int[] resultValues, boolean backTranslate
  );

  private final TranslationTable translationTable;
  private final boolean translationSucceeded;

  private final CharSequence suppliedInput;
  private final CharSequence consumedInput;
  private final int[] inputOffsets;
  private final Integer inputCursor;

  private final char[] outputArray;
  private String outputString = null;
  private CharSequence outputWithSpans = null;
  private final int[] outputOffsets;
  private final Integer outputCursor;

  public final TranslationTable getTranslationTable () {
    return translationTable;
  }

  public final CharSequence getSuppliedInput () {
    return suppliedInput;
  }

  public final CharSequence getConsumedInput () {
    return consumedInput;
  }

  public final int getInputLength () {
    return consumedInput.length();
  }

  public final int getInputOffset (int outputOffset) {
    if (outputOffset == getOutputLength()) return getInputLength();
    return inputOffsets[outputOffset];
  }

  public final int findFirstInputOffset (int outputOffset) {
    int inputOffset = getInputOffset(outputOffset);

    while (inputOffset > 0) {
      int next = inputOffset - 1;
      if (getOutputOffset(next) != outputOffset) break;
      inputOffset = next;
    }

    return inputOffset;
  }

  public final int findLastInputOffset (int outputOffset) {
    final int inputLength = getInputLength();
    int inputOffset = getInputOffset(outputOffset);

    while (inputOffset < inputLength) {
      int next = inputOffset + 1;
      if (getOutputOffset(next) != outputOffset) break;
      inputOffset = next;
    }

    return inputOffset;
  }

  public final int findEndInputOffset (int outputOffset) {
    if (outputOffset == 0) return 0;
    return findLastInputOffset(outputOffset-1) + 1;
  }

  public final Integer getInputCursor () {
    return inputCursor;
  }

  public final char[] getOutputAsArray () {
    return outputArray;
  }

  public final String getOutputAsString () {
    synchronized (this) {
      if (outputString == null) {
        outputString = new String(outputArray);
      }
    }

    return outputString;
  }

  private final void copyInputSpans (SpannableStringBuilder sb) {
    CharSequence input = consumedInput;

    if (input instanceof Spanned) {
      Spanned spanned = (Spanned)input;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        for (Object span : spans) {
          int start = getOutputOffset(spanned.getSpanStart(span));
          int end = getOutputOffset(spanned.getSpanEnd(span));
          int flags = spanned.getSpanFlags(span);
          sb.setSpan(span, start, end, flags);
        }
      }
    }
  }

  public final CharSequence getOutputWithSpans () {
    synchronized (this) {
      if (outputWithSpans == null) {
        SpannableStringBuilder sb = new SpannableStringBuilder(getOutputAsString());
        copyInputSpans(sb);
        outputWithSpans = sb.subSequence(0, sb.length());
      }
    }

    return outputWithSpans;
  }

  public final int getOutputLength () {
    return outputArray.length;
  }

  public final int getOutputOffset (int inputOffset) {
    if (inputOffset == getInputLength()) return getOutputLength();
    return outputOffsets[inputOffset];
  }

  public final int findFirstOutputOffset (int inputOffset) {
    int outputOffset = getOutputOffset(inputOffset);

    while (outputOffset > 0) {
      int next = outputOffset - 1;
      if (getInputOffset(next) != inputOffset) break;
      outputOffset = next;
    }

    return outputOffset;
  }

  public final int findLastOutputOffset (int inputOffset) {
    final int outputLength = getOutputLength();
    int outputOffset = getOutputOffset(inputOffset);

    while (outputOffset < outputLength) {
      int next = outputOffset + 1;
      if (getInputOffset(next) != inputOffset) break;
      outputOffset = next;
    }

    return outputOffset;
  }

  public final int findEndOutputOffset (int inputOffset) {
    if (inputOffset == 0) return 0;
    return findLastOutputOffset(inputOffset-1) + 1;
  }

  public final Integer getOutputCursor () {
    return outputCursor;
  }

  private final static byte ITALIC    = 0X1;
  private final static byte UNDERLINE = 0X2;
  private final static byte BOLD      = 0X4;

  private static byte[] createTypeForm (CharSequence text) {
    final int length = text.length();
    final byte[] typeForm = new byte[length];
    for (int index=0; index<length; index+=1) typeForm[index] = 0;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        for (Object span : spans) {
          byte flags = 0;

          if (span instanceof UnderlineSpan) {
            flags = UNDERLINE;
          } else if (span instanceof StyleSpan) {
            switch (((StyleSpan)span).getStyle()) {
              case Typeface.BOLD:
                flags = BOLD;
                break;

              case Typeface.ITALIC:
                flags = ITALIC;
                break;

              case Typeface.BOLD_ITALIC:
                flags = BOLD | ITALIC;
                break;
            }
          }

          if (flags != 0) {
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

  private enum ResultValuesIndex {
    INPUT_LENGTH,
    OUTPUT_LENGTH,
    CURSOR_OFFSET,
    ; // end of enumeration
  }

  private final static int RESULT_VALUES_COUNT = ResultValuesIndex.values().length;
  private final static int RVI_INPUT_LENGTH = ResultValuesIndex.INPUT_LENGTH.ordinal();
  private final static int RVI_OUTPUT_LENGTH = ResultValuesIndex.OUTPUT_LENGTH.ordinal();
  private final static int RVI_CURSOR_OFFSET = ResultValuesIndex.CURSOR_OFFSET.ordinal();

  public Translation (
    TranslationTable table, CharSequence input,
    int outputLength, int cursorOffset,
    boolean backTranslate
  ) {
    int inputLength = input.length();

    translationTable = table;
    suppliedInput = input;
    inputCursor = (cursorOffset < 0)? null: Integer.valueOf(cursorOffset);

    String inputString = input.toString();
    char[] output = new char[outputLength];
    int[] outOffsets = new int[inputLength];
    int[] inOffsets = new int[outputLength];

    int[] resultValues = new int[RESULT_VALUES_COUNT];
    resultValues[RVI_INPUT_LENGTH] = inputLength;
    resultValues[RVI_OUTPUT_LENGTH] = outputLength;
    resultValues[RVI_CURSOR_OFFSET] = cursorOffset;

    synchronized (Louis.NATIVE_LOCK) {
      translationSucceeded = translate(
        table.getFileName(), inputString, output, createTypeForm(input),
        outOffsets, inOffsets, resultValues, backTranslate
      );
    }

    if (!translationSucceeded) {
      Log.w(LOG_TAG, "translation failed");

      if (resultValues[RVI_INPUT_LENGTH] > resultValues[RVI_OUTPUT_LENGTH]) {
        resultValues[RVI_INPUT_LENGTH] = resultValues[RVI_OUTPUT_LENGTH];
      } else if (resultValues[RVI_OUTPUT_LENGTH] > resultValues[RVI_INPUT_LENGTH]) {
        resultValues[RVI_OUTPUT_LENGTH] = resultValues[RVI_INPUT_LENGTH];
      }

      for (int offset=0; offset<resultValues[RVI_INPUT_LENGTH]; offset+=1) {
        inOffsets[offset] = outOffsets[offset] = offset;
      }

      if (resultValues[RVI_CURSOR_OFFSET] >= resultValues[RVI_INPUT_LENGTH]) {
        resultValues[RVI_CURSOR_OFFSET] = -1;
      }

      inputString.getChars(0, inputString.length(), output, 0);
    }

    int newInputLength  = resultValues[RVI_INPUT_LENGTH];
    int newOutputLength = resultValues[RVI_OUTPUT_LENGTH];
    int newCursorOffset = resultValues[RVI_CURSOR_OFFSET];

    if (newInputLength < inputLength) {
      input = input.subSequence(0, newInputLength);
      outOffsets = Arrays.copyOf(outOffsets, newInputLength);
    }

    if (newOutputLength < outputLength) {
      output = Arrays.copyOf(output, newOutputLength);
      inOffsets = Arrays.copyOf(inOffsets, newOutputLength);
    }

    consumedInput = input;
    outputArray = output;
    outputOffsets = outOffsets;
    inputOffsets = inOffsets;
    outputCursor = (newCursorOffset < 0)? null: Integer.valueOf(newCursorOffset);
  }
}
