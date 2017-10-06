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

  public final int findFirstTextOffset (int textOffset) {
    return findFirstInputOffset(textOffset);
  }

  public final int findLastTextOffset (int textOffset) {
    return findLastInputOffset(textOffset);
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

  public final int findFirstBrailleOffset (int brailleOffset) {
    return findFirstOutputOffset(brailleOffset);
  }

  public final int findLastBrailleOffset (int brailleOffset) {
    return findLastOutputOffset(brailleOffset);
  }

  public final Integer getBrailleCursor () {
    return getOutputCursor();
  }

  public BrailleTranslation (TranslationBuilder builder) {
    super(builder, false);
  }
}
