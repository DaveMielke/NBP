package org.nbp.calculator;

public enum DecimalNotation {
  SCIENTIFIC("SCI", R.string.description_decimalNotation_scientific,
    new ComplexFormatter () {
    }
  ),

  ENGINEERING("ENG", R.string.description_decimalNotation_engineering,
    new ComplexFormatter () {
      {
        setDecimalGrouping(3);
      }
    }
  ),

  FIXED("FXD", R.string.description_decimalNotation_fixed,
    new ComplexFormatter () {
      {
        setGroupingSeparatorEnabled(true);
        setMaximumFixedDigits(12);
        setMinimumFixedDigits(-2);
      }
    }
  );

  public final int getTitle () {
    return R.string.title_decimalNotation;
  }

  private final String notationLabel;
  private final int notationDescription;
  private final ComplexFormatter complexFormatter;

  public final String getLabel () {
    return notationLabel;
  }

  public final int getDescription () {
    return notationDescription;
  }

  public final ComplexFormatter getComplexFormatter () {
    return complexFormatter;
  }

  private DecimalNotation (String label, int description, ComplexFormatter formatter) {
    notationLabel = label;
    notationDescription = description;
    complexFormatter = formatter;
  }
}
