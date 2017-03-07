package org.nbp.calculator;

public enum CalculatorMode {
  COMPLEX(
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

  private final Properties properties;

  public final ExpressionEvaluator newExpressionEvaluator (
    String expression
  ) throws ExpressionException {
    return properties.newExpressionEvaluator(expression);
  }

  private CalculatorMode (Properties properties) {
    this.properties = properties;
  }
}
