package org.liblouis;

public abstract class Translator extends TranslationComponent {
  protected Translator () {
  }

  public abstract boolean translate (
    CharSequence inputBuffer, char[] outputBuffer,
    int[] outputOffsets, int[] inputOffsets,
    boolean backTranslate, boolean includeHighlighting,
    int[] resultValues
  );
}
