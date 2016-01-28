package org.liblouis;

public class BrailleTranslation extends Translation {
  public final CharSequence getSuppliedText () {
    return getSuppliedInput();
  }

  public final CharSequence getConsumedText () {
    return getConsumedInput();
  }

  public final int getTextLength () {
    return getInputLength();
  }

  public final int getTextOffset (int brailleOffset) {
    return getInputOffset(brailleOffset);
  }

  public final int findFirstTextOffset (int brailleOffset) {
    return findFirstInputOffset(brailleOffset);
  }

  public final int findLastTextOffset (int brailleOffset) {
    return findLastInputOffset(brailleOffset);
  }

  public final int findEndTextOffset (int brailleOffset) {
    return findEndInputOffset(brailleOffset);
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

  public final int getBrailleLength () {
    return getOutputLength();
  }

  public final int getBrailleOffset (int textOffset) {
    return getOutputOffset(textOffset);
  }

  public final int findFirstBrailleOffset (int textOffset) {
    return findFirstOutputOffset(textOffset);
  }

  public final int findLastBrailleOffset (int textOffset) {
    return findLastOutputOffset(textOffset);
  }

  public final int findEndBrailleOffset (int textOffset) {
    return findEndOutputOffset(textOffset);
  }

  public final Integer getBrailleCursor () {
    return getOutputCursor();
  }

  public BrailleTranslation (
    TranslationTable table, CharSequence text,
    int brailleLength, int cursorOffset
  ) {
    super(table, text, brailleLength, cursorOffset, false);
  }
}
