package org.nbp.calculator;

import java.util.Arrays;

public enum CalculatorMode {
  DECIMAL("DEC", R.string.description_calculatorMode_decimal,
    new ComplexEvaluator(),

    new Keypad[] {
      Keypad.DECIMAL,
      Keypad.FUNCTION,
      Keypad.CONVERSION
    },

    new Properties() {
      @Override
      public AbstractNumber newNumber (String string) {
        return ComplexNumber.valueOf(string);
      }
    }
  ),

  HEXADECIMAL("HEX", R.string.description_calculatorMode_hexadecimal,
    new HexadecimalEvaluator(),

    new Keypad[] {
      Keypad.HEXADECIMAL
    },

    new Properties() {
      @Override
      public AbstractNumber newNumber (String string) {
        return HexadecimalNumber.valueOf(string);
      }
    }
  );

  private interface Properties {
    public AbstractNumber newNumber (String string);
  }

  public final int getTitle () {
    return R.string.title_calculatorMode;
  }

  private final String modeLabel;
  private final int modeDescription;
  private final ExpressionEvaluator expressionEvaluator;
  private final Keypad[] activeKeypads;
  private final Properties modeProperties;

  public final String getLabel () {
    return modeLabel;
  }

  public final int getDescription () {
    return modeDescription;
  }

  public final ExpressionEvaluator getEvaluator () {
    return expressionEvaluator;
  }

  public final Keypad[] getActiveKeypads () {
    return Arrays.copyOf(activeKeypads, activeKeypads.length);
  }

  public final AbstractNumber newNumber (String string) {
    return modeProperties.newNumber(string);
  }

  private CalculatorMode (
    String label, int description,
    ExpressionEvaluator evaluator, Keypad[] keypads,
    Properties properties
  ) {
    modeLabel = label;
    modeDescription = description;
    expressionEvaluator = evaluator;
    activeKeypads = keypads;
    modeProperties = properties;
  }
}
