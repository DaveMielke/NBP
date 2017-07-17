package org.nbp.b2g.ui;

import org.nbp.common.Braille;

public enum IndicatorOverlay {
  DOTS_78(Braille.CELL_DOT_7 | Braille.CELL_DOT_8),
  DOT_7(Braille.CELL_DOT_7),
  DOT_8(Braille.CELL_DOT_8),
  ;

  private final byte indicatorDots;

  private IndicatorOverlay (int dots) {
    indicatorDots = (byte)dots;
  }

  public byte getDots () {
    return indicatorDots;
  }
}
