package org.nbp.b2g.ui;

public enum GenericLevel {
  LOWEST,
  LOW,
  MEDIUM,
  HIGH,
  HIGHEST;

  private final static int MAXIMUM_VALUE = values().length - 1;

  public final int getValue (int minimum, int maximum) {
    return (ordinal() * (maximum - minimum) / MAXIMUM_VALUE) + minimum;
  }
}
