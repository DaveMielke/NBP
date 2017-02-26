package org.nbp.calculator;

public enum Notation {
  SCIENTIFIC("SCI", R.string.description_notation_scientific,
    new ComplexFormatter () {
    }
  ),

  ENGINEERING("ENG", R.string.description_notation_engineering,
    new ComplexFormatter () {
      {
        setDecimalGrouping(3);
      }
    }
  ),

  FIXED("FXD", R.string.description_notation_fixed,
    new ComplexFormatter () {
      {
        setGroupingSeparatorEnabled(true);
        setMaximumFixedDigits(12);
        setMinimumFixedDigits(-2);
      }
    }
  );

  public final int getTitle () {
    return R.string.title_notation;
  }

  private final String notationLabel;
  private final int notationDescription;
  private final ComplexFormatter notationFormatter;

  public final String getLabel () {
    return notationLabel;
  }

  public final int getDescription () {
    return notationDescription;
  }

  public final ComplexFormatter getFormatter () {
    return notationFormatter;
  }

  private Notation (String label, int description, ComplexFormatter formatter) {
    notationLabel = label;
    notationDescription = description;
    notationFormatter = formatter;
  }
}
