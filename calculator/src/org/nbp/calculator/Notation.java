package org.nbp.calculator;

public enum Notation {
  SCIENTIFIC("SCI", "Scientific"),
  ENGINEERING("ENG", "Engineering"),
  FIXED("FXD", "Fixed");

  private final String notationLabel;
  private final String notationName;

  public final String getLabel () {
    return notationLabel;
  }

  public final String getName () {
    return notationName;
  }

  private Notation (String label, String name) {
    notationLabel = label;
    notationName = name;
  }
}
