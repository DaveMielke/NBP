package org.liblouis;

public class BackTranslationSpan {
  private final CharSequence originalText;

  public final CharSequence getOriginalText () {
    return originalText;
  }

  public BackTranslationSpan (CharSequence text) {
    originalText = text;
  }
}
