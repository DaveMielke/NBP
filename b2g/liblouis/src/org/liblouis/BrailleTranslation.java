package org.liblouis;

public class BrailleTranslation extends Translation {
  public final CharSequence getSuppliedText () {
    return getSuppliedInput();
  }

  public final CharSequence getConsumedText () {
    return getConsumedInput();
  }

  public final int getTextOffset (int brailleOffset) {
    return getInputOffset(brailleOffset);
  }

  public final Integer getTextCursor () {
    return getInputCursor();
  }

  public final char[] getBrailleAsArray () {
    return getOutputAsArray();
  }

  public final String getBrailleAsString () {
    return getOutputAsString();
  }

  public final CharSequence getBrailleWithSpans () {
    return getOutputWithSpans();
  }

  public final int getBrailleOffset (int textOffset) {
    return getOutputOffset(textOffset);
  }

  public final Integer getBrailleCursor () {
    return getOutputCursor();
  }

  public BrailleTranslation (String table, CharSequence text, int brailleLength, int cursorOffset) {
    super(table, text, brailleLength, cursorOffset, false);
  }
}
