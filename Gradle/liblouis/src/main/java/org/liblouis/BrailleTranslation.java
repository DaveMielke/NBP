package org.liblouis;

public class BrailleTranslation extends Translation {
  @Override
  public final String getInputTag () {
    return TEXT_TAG;
  }

  @Override
  public final String getOutputTag () {
    return BRAILLE_TAG;
  }

  public final CharSequence getSuppliedText () {
    return getSuppliedInput();
  }

  public final CharSequence getConsumedText () {
    return getConsumedInput();
  }

  @Override
  public final int getTextLength () {
    return getInputLength();
  }

  @Override
  public final int getTextOffset (int brailleOffset) {
    return getInputOffset(brailleOffset);
  }

  @Override
  public final int findFirstTextOffset (int textOffset) {
    return findFirstInputOffset(textOffset);
  }

  @Override
  public final int findLastTextOffset (int textOffset) {
    return findLastInputOffset(textOffset);
  }

  @Override
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

  @Override
  public final int getBrailleLength () {
    return getOutputLength();
  }

  @Override
  public final int getBrailleOffset (int textOffset) {
    return getOutputOffset(textOffset);
  }

  @Override
  public final int findFirstBrailleOffset (int brailleOffset) {
    return findFirstOutputOffset(brailleOffset);
  }

  @Override
  public final int findLastBrailleOffset (int brailleOffset) {
    return findLastOutputOffset(brailleOffset);
  }

  @Override
  public final Integer getBrailleCursor () {
    return getOutputCursor();
  }

  public BrailleTranslation (TranslationBuilder builder) {
    super(builder, false);
  }
}
