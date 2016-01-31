package org.nbp.b2g.ui;

public enum TypingMode {
  TEXT(
    R.string.enum_TypingMode_TEXT
  ),

  BRAILLE(
    R.string.enum_TypingMode_BRAILLE
  );

  private TypingMode (int label) {
    EnumerationLabels.setLabel(this, label);
  }
}
