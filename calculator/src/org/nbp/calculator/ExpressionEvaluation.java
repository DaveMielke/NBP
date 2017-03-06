package org.nbp.calculator;

public abstract class ExpressionEvaluation<T> extends ExpressionParser {
  protected abstract T evaluateExpression () throws ExpressionException;
  private final T expressionResult;

  public final T getResult () {
    return expressionResult;
  }

  protected boolean verifyValue (T value) {
    return true;
  }

  protected final T evaluateSubexpression () throws ExpressionException {
    int start = getCurrentToken().getStart();
    nextToken();

    if (getTokenType() == TokenType.CLOSE) {
      throw new EvaluationException(R.string.error_missing_subexpression, start);
    }

    T value = evaluateExpression();

    if (getTokenType() != TokenType.CLOSE) {
      throw new EvaluationException(R.string.error_unclosed_bracket, start);
    }

    nextToken();
    return value;
  }

  protected T evaluateElement () throws ExpressionException {
    switch (getTokenType()) {
      default: {
        throw new EvaluationException(R.string.error_missing_element);
      }
    }
  }

  protected ExpressionEvaluation (String expression) throws ExpressionException {
    super(expression);

    int end = expressionText.length();
    if (getCurrentToken() == null) throw new NoExpressionException(end);

    if (getTokenType() != TokenType.CLOSE) {
      expressionResult = evaluateExpression();
      TokenDescriptor token = getCurrentToken();

      if (token == null) {
        if (verifyValue(expressionResult)) return;
        throw new EvaluationException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new EvaluationException(R.string.error_missing_operator, token);
      }
    }

    throw new EvaluationException(R.string.error_unopened_bracket);
  }
}
