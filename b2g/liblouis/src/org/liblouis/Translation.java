package org.liblouis;

import java.util.Arrays;

import android.util.Log;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

public class Translation {
  private final static String LOG_TAG = Translation.class.getName();

  private native boolean translate (
    String tableName, String inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets, int[] resultValues,
    boolean backTranslate
  );

  private final TranslationTable translationTable;

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

  public final Integer getOutputCursor () {
    return outputCursor;
  }

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
    int[] resultValues = new int[] {inputLength, outputLength, cursorOffset};

    if (!translate(table.getFileName(), inputString, output,
                   outOffsets, inOffsets, resultValues, backTranslate)) {
      Log.w(LOG_TAG, "translation failed");

      if (resultValues[0] > resultValues[1]) {
        resultValues[0] = resultValues[1];
      } else if (resultValues[1] > resultValues[0]) {
        resultValues[1] = resultValues[0];
      }

      for (int offset=0; offset<resultValues[0]; offset+=1) {
        inOffsets[offset] = outOffsets[offset] = offset;
      }

      inputString.getChars(0, inputString.length(), output, 0);
      if (resultValues[2] >= resultValues[0]) resultValues[2] = -1;
    }

    int newInputLength  = resultValues[0];
    int newOutputLength = resultValues[1];
    int newCursorOffset = resultValues[2];

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
