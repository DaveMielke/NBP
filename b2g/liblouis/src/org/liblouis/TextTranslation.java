package org.liblouis;

public class TextTranslation extends Translation {
  public final CharSequence getSuppliedBraille () {
    return getSuppliedInput();
  }

  public final CharSequence getConsumedBraille () {
    return getConsumedInput();
  }

  public final int getBrailleLength () {
    return getInputLength();
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

  public final int getTextLength () {
    return getOutputLength();
  }

  public final int getTextOffset (int brailleOffset) {
    return getOutputOffset(brailleOffset);
  }

  public final int findFirstTextOffset (int brailleOffset) {
    return findFirstOutputOffset(brailleOffset);
  }

  public final int findLastTextOffset (int brailleOffset) {
    return findLastOutputOffset(brailleOffset);
  }

  public final int findEndTextOffset (int brailleOffset) {
    return findEndOutputOffset(brailleOffset);
  }

  public final Integer getTextCursor () {
    return getOutputCursor();
  }

  public TextTranslation (
    TranslationTable table, CharSequence braille,
    int textLength, int cursorOffset
  ) {
    super(table, braille, textLength, cursorOffset, true);
  }
}
