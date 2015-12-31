package org.liblouis;

import java.util.Arrays;

import android.util.Log;

public class Translation {
  private final static String LOG_TAG = Translation.class.getName();

  private native boolean translate (
    String table, String text, char[] braille,
    int[] outputOffsets, int[] inputOffsets, int[] resultValues
  );

  private final String tableName;
  private final CharSequence suppliedText;
  private final Integer textCursor;

  private final CharSequence consumedText;
  private final char[] brailleCharacters;
  private final int[] brailleOffsets;
  private final int[] textOffsets;
  private final Integer brailleCursor;

  public final String getTableName () {
    return tableName;
  }

  public final CharSequence getSuppliedText () {
    return suppliedText;
  }

  public final Integer getTextCursor () {
    return textCursor;
  }

  public final CharSequence getConsumedText () {
    return consumedText;
  }

  public final char[] getBrailleCharacters () {
    return brailleCharacters;
  }

  public final int getBrailleOffset (int textOffset) {
    return brailleOffsets[textOffset];
  }

  public final int getTextOffset (int brailleOffset) {
    return textOffsets[brailleOffset];
  }

  public final Integer getBrailleCursor () {
    return brailleCursor;
  }

  public Translation (String table, CharSequence text, int brailleLength, int cursorOffset) {
    int textLength = text.length();

    tableName = table;
    suppliedText = text;
    textCursor = (cursorOffset < 0)? null: Integer.valueOf(cursorOffset);

    char[] braille = new char[brailleLength];
    int[] outputOffsets = new int[textLength];
    int[] inputOffsets = new int[brailleLength];
    int[] resultValues = new int[] {textLength, brailleLength, cursorOffset};

    if (!translate(tableName, text.toString(), braille,
                   outputOffsets, inputOffsets, resultValues)) {
    //throw new TranslationFailedException(text);
    }

    int newTextLength    = resultValues[0];
    int newBrailleLength = resultValues[1];
    int newCursorOffset  = resultValues[2];

    if (newTextLength < textLength) {
      text = text.subSequence(0, newTextLength);
      outputOffsets = Arrays.copyOf(outputOffsets, newTextLength);
    }

    if (newBrailleLength < brailleLength) {
      braille = Arrays.copyOf(braille, newBrailleLength);
      inputOffsets = Arrays.copyOf(inputOffsets, newBrailleLength);
    }

    consumedText = text;
    brailleCharacters = braille;
    textOffsets = outputOffsets;
    brailleOffsets = inputOffsets;
    brailleCursor = (newCursorOffset < 0)? null: Integer.valueOf(newCursorOffset);
  }
}
