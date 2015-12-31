package org.liblouis;

public class TextTranslation extends Translation {
  public final CharSequence getSuppliedBraille () {
    return getSuppliedInput();
  }

  public final Integer getBrailleCursor () {
    return getInputCursor();
  }

  public final CharSequence getConsumedBraille () {
    return getConsumedInput();
  }

  public final char[] getTextCharacters () {
    return getOutputCharacters();
  }

  public final int getTextOffset (int brailleOffset) {
    return getOutputOffset(brailleOffset);
  }

  public final int getBrailleOffset (int textOffset) {
    return getInputOffset(textOffset);
  }

  public final Integer getTextCursor () {
    return getOutputCursor();
  }

  public TextTranslation (String table, CharSequence braille, int textLength, int cursorOffset) {
    super(table, braille, textLength, cursorOffset, true);
  }
}
