package org.nbp.b2g.ui;

public enum InputMode {
  TEXT(
    R.string.enum_InputMode_TEXT
  ),

  BRAILLE(
    R.string.enum_InputMode_BRAILLE
  );

  private InputMode (int label) {
    EnumerationLabels.setLabel(this, label);
  }
}
