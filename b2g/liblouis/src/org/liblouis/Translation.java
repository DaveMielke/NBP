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

  private final String tableName;

  private final CharSequence suppliedInput;
  private final CharSequence consumedInput;
  private final int[] inputOffsets;
  private final Integer inputCursor;

  private final char[] outputArray;
  private String outputString = null;
  private CharSequence outputWithSpans = null;
  private final int[] outputOffsets;
  private final Integer outputCursor;

  public final String getTableName () {
    return tableName;
  }

  public final CharSequence getSuppliedInput () {
    return suppliedInput;
  }

  public final CharSequence getConsumedInput () {
    return consumedInput;
  }

  public final int getInputOffset (int outputOffset) {
    if (outputOffset == outputArray.length) return consumedInput.length();
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

  public final CharSequence getOutputWithSpans () {
    synchronized (this) {
      if (outputWithSpans == null) {
        CharSequence input = consumedInput;

        if (input instanceof Spanned) {
          Spanned spanned = (Spanned)input;
          Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

          if (spans != null) {
            if (spans.length > 0) {
              SpannableStringBuilder sb = new SpannableStringBuilder(getOutputAsString());

              for (Object span : spans) {
                int start = getOutputOffset(spanned.getSpanStart(span));
                int end = getOutputOffset(spanned.getSpanEnd(span));
                int flags = spanned.getSpanFlags(span);
                sb.setSpan(span, start, end, flags);
              }

              outputWithSpans = sb.subSequence(0, sb.length());
            }
          }
        }

        if (outputWithSpans == null) {
          outputWithSpans = getOutputAsString();
        }
      }
    }

    return outputWithSpans;
  }

  public final int getOutputOffset (int inputOffset) {
    if (inputOffset == consumedInput.length()) return outputArray.length;
    return outputOffsets[inputOffset];
  }

  public final Integer getOutputCursor () {
    return outputCursor;
  }

  public Translation (
    String table, CharSequence input, int outputLength,
    int cursorOffset, boolean backTranslate
  ) {
    int inputLength = input.length();

    tableName = table;
    suppliedInput = input;
    inputCursor = (cursorOffset < 0)? null: Integer.valueOf(cursorOffset);

    char[] output = new char[outputLength];
    int[] outOffsets = new int[inputLength];
    int[] inOffsets = new int[outputLength];
    int[] resultValues = new int[] {inputLength, outputLength, cursorOffset};

    if (!translate(tableName, input.toString(), output,
                   outOffsets, inOffsets, resultValues, backTranslate)) {
      Log.w(LOG_TAG, "translation failed");
    //throw new TranslationFailedException(input);
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
