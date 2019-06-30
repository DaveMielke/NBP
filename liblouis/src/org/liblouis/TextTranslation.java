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

  @Override
  public final int getBrailleOffset (int textOffset) {
    return getInputOffset(textOffset);
  }

  @Override
  public final int findFirstBrailleOffset (int brailleOffset) {
    return findFirstInputOffset(brailleOffset);
  }

  @Override
  public final int findLastBrailleOffset (int brailleOffset) {
    return findLastInputOffset(brailleOffset);
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

  @Override
  public final int getTextOffset (int brailleOffset) {
    return getOutputOffset(brailleOffset);
  }

  @Override
  public final int findFirstTextOffset (int textOffset) {
    return findFirstOutputOffset(textOffset);
  }

  @Override
  public final int findLastTextOffset (int textOffset) {
    return findLastOutputOffset(textOffset);
  }

  public final Integer getTextCursor () {
    return getOutputCursor();
  }

  public TextTranslation (TranslationBuilder builder) {
    super(builder, true);
  }
}
