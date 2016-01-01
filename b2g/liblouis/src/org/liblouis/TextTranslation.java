package org.liblouis;

public class TextTranslation extends Translation {
  public final CharSequence getSuppliedBraille () {
    return getSuppliedInput();
  }

  public final CharSequence getConsumedBraille () {
    return getConsumedInput();
  }

  public final int getBrailleOffset (int textOffset) {
    return getInputOffset(textOffset);
  }

  public final Integer getBrailleCursor () {
    return getInputCursor();
  }

  public final char[] getTextAsArray () {
    return getOutputAsArray();
  }

  public final String getTextAsString () {
    return getOutputAsString();
  }

  public final CharSequence getTextWithSpans () {
    return getOutputWithSpans();
  }

  public final int getTextOffset (int brailleOffset) {
    return getOutputOffset(brailleOffset);
  }

  public final Integer getTextCursor () {
    return getOutputCursor();
  }

  public TextTranslation (String table, CharSequence braille, int textLength, int cursorOffset) {
    super(table, braille, textLength, cursorOffset, true);
  }
}
