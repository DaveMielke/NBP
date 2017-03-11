package org.nbp.calculator;

import java.util.Arrays;

public enum CalculatorMode {
  DECIMAL("DEC", R.string.description_calculatorMode_decimal,
    new Keypad[] {
      Keypad.DECIMAL,
      Keypad.FUNCTION
    },

    new Properties() {
      @Override
      public GenericNumber newNumber (String string) {
        return ComplexNumber.valueOf(string);
      }

      @Override
      public ExpressionEvaluator newEvaluator (
        String expression
      ) throws ExpressionException {
        return new ComplexEvaluator(expression);
      }
    }
  ),

  HEXADECIMAL("HEX", R.string.description_calculatorMode_hexadecimal,
    new Keypad[] {
      Keypad.HEXADECIMAL
    },

    new Properties() {
      @Override
      public GenericNumber newNumber (String string) {
        return HexadecimalNumber.valueOf(string);
      }

      @Override
      public ExpressionEvaluator newEvaluator (
        String expression
      ) throws ExpressionException {
        return new HexadecimalEvaluator(expression);
      }
    }
  );

  private interface Properties {
    public GenericNumber newNumber (String string);

    public ExpressionEvaluator newEvaluator (
      String expression
    ) throws ExpressionException;
  }

  public final int getTitle () {
    return R.string.title_calculatorMode;
  }

  private final String modeLabel;
  private final int modeDescription;
  private final Keypad[] modeKeypads;
  private final Properties modeProperties;

  public final String getLabel () {
    return modeLabel;
  }

  public final int getDescription () {
    return modeDescription;
  }

  public final Keypad[] getKeypads () {
    return Arrays.copyOf(modeKeypads, modeKeypads.length);
  }

  public final GenericNumber newNumber (String string) {
    return modeProperties.newNumber(string);
  }

  public final ExpressionEvaluator newEvaluator (
    String expression
  ) throws ExpressionException {
    return modeProperties.newEvaluator(expression);
  }

  private CalculatorMode (String label, int description, Keypad[] keypads, Properties properties) {
    modeLabel = label;
    modeDescription = description;
    modeKeypads = keypads;
    modeProperties = properties;
  }
}
