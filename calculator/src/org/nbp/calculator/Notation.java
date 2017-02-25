package org.nbp.calculator;

public enum Notation {
  SCIENTIFIC("SCI", "Scientific",
    new ComplexFormatter () {
    }
  ),

  ENGINEERING("ENG", "Engineering",
    new ComplexFormatter () {
    }
  ),

  FIXED("FXD", "Fixed",
    new ComplexFormatter () {
    }
  );

  private final String notationLabel;
  private final String notationDescription;
  private final ComplexFormatter notationFormatter;

  public final String getLabel () {
    return notationLabel;
  }

  public final String getDescription () {
    return notationDescription;
  }

  public final ComplexFormatter getFormatter () {
    return notationFormatter;
  }

  private Notation (String label, String description, ComplexFormatter formatter) {
    notationLabel = label;
    notationDescription = description;
    notationFormatter = formatter;
  }
}
