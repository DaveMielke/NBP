package org.nbp.b2g.ui;

public enum GenericLevel {
  LOWEST,
  LOW,
  MEDIUM,
  HIGH,
  HIGHEST,
  ; // end of enumeration

  private final static int MAXIMUM_VALUE = values().length - 1;

  public final int getValue (int minimum, int maximum) {
    return (ordinal() * (maximum - minimum) / MAXIMUM_VALUE) + minimum;
  }
}
