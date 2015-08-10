package org.nbp.b2g.ui;

public enum IndicatorOverlay {
  DOTS_78(
    R.string.enum_IndicatorOverlay_DOTS_78,
    BrailleDevice.DOT_7 | BrailleDevice.DOT_8
  ),

  DOT_7(
    R.string.enum_IndicatorOverlay_DOT_7,
    BrailleDevice.DOT_7
  ),

  DOT_8(
    R.string.enum_IndicatorOverlay_DOT_8,
    BrailleDevice.DOT_8
  );

  private final byte dots;

  public byte getDots () {
    return dots;
  }

  private IndicatorOverlay (int label, int dots) {
    EnumerationLabels.setLabel(this, label);
    this.dots = (byte)dots;
  }
}
