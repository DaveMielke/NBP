package org.nbp.calculator;

public enum Notation {
  SCIENTIFIC("SCI", "Scientific"),
  ENGINEERING("ENG", "Engineering"),
  FIXED("FXD", "Fixed");

  private final String notationLabel;
  private final String notationDescription;

  public final String getLabel () {
    return notationLabel;
  }

  public final String getDescription () {
    return notationDescription;
  }

  private Notation (String label, String description) {
    notationLabel = label;
    notationDescription = description;
  }
}
