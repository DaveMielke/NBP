package org.nbp.b2g.ui;

public enum IndicatorOverlay {
  DOTS_78(BrailleDevice.DOT_7 | BrailleDevice.DOT_8),
  DOT_7(BrailleDevice.DOT_7),
  DOT_8(BrailleDevice.DOT_8),
  ;

  private final byte indicatorDots;

  private IndicatorOverlay (int dots) {
    indicatorDots = (byte)dots;
  }

  public byte getDots () {
    return indicatorDots;
  }
}
