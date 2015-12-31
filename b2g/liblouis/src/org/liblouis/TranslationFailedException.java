package org.liblouis;

public class TranslationFailedException extends RuntimeException {
  private final CharSequence text;

  public TranslationFailedException (CharSequence text) {
    super("translation failed");
    this.text = text;
  }
}
