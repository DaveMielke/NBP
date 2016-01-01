package org.liblouis;

import java.util.Arrays;

import android.util.Log;

public class Translation {
  private final static String LOG_TAG = Translation.class.getName();

  private native boolean translate (
    String tableName, String inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets, int[] resultValues,
    boolean backTranslate
  );

  private final String tableName;
  private final CharSequence suppliedInput;
  private final Integer inputCursor;

  private final CharSequence consumedInput;
  private final char[] outputCharacters;
  private final int[] outputOffsets;
  private final int[] inputOffsets;
  private final Integer outputCursor;

  public final String getTableName () {
    return tableName;
  }

  public final CharSequence getSuppliedInput () {
    return suppliedInput;
  }

  public final Integer getInputCursor () {
    return inputCursor;
  }

  public final CharSequence getConsumedInput () {
    return consumedInput;
  }

  public final char[] getOutputAsArray () {
    return outputCharacters;
  }

  public final String getOutputAsString () {
    return new String(getOutputAsArray());
  }

  public final int getOutputOffset (int inputOffset) {
    if (inputOffset == consumedInput.length()) return outputCharacters.length;
    return outputOffsets[inputOffset];
  }

  public final int getInputOffset (int outputOffset) {
    if (outputOffset == outputCharacters.length) return consumedInput.length();
    return inputOffsets[outputOffset];
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
    outputCharacters = output;
    outputOffsets = outOffsets;
    inputOffsets = inOffsets;
    outputCursor = (newCursorOffset < 0)? null: Integer.valueOf(newCursorOffset);
  }
}
