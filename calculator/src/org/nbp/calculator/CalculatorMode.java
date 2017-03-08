package org.nbp.calculator;

import java.util.Arrays;

public enum CalculatorMode {
  COMPLEX(
    new Keypad[] {
      Keypad.DECIMAL,
      Keypad.FUNCTION
    },

    new Properties() {
      @Override
      public ExpressionEvaluator newExpressionEvaluator (
        String expression
      ) throws ExpressionException {
        return new ComplexEvaluator(expression);
      }
    }
  );

  private interface Properties {
    public ExpressionEvaluator newExpressionEvaluator (
      String expression
    ) throws ExpressionException;
  }

  private final Keypad[] keypads;
  private final Properties properties;

  public final Keypad[] getKeypads () {
    return Arrays.copyOf(keypads, keypads.length);
  }

  public final ExpressionEvaluator newExpressionEvaluator (
    String expression
  ) throws ExpressionException {
    return properties.newExpressionEvaluator(expression);
  }

  private CalculatorMode (Keypad[] keypads, Properties properties) {
    this.keypads = keypads;
    this.properties = properties;
  }
}
