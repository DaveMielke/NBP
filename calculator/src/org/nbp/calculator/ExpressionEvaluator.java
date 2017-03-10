package org.nbp.calculator;

public abstract class ExpressionEvaluator<T extends GenericNumber> extends ExpressionParser {
  protected abstract T evaluateExpression () throws ExpressionException;
  private final T expressionResult;

  public final T getResult () {
    return expressionResult;
  }

  protected final T evaluateSubexpression () throws ExpressionException {
    int start = getCurrentToken().getStart();
    nextToken();

    if (getTokenType() == TokenType.CLOSE) {
      throw new EvaluateException(R.string.error_missing_subexpression, start);
    }

    T value = evaluateExpression();

    if (getTokenType() != TokenType.CLOSE) {
      throw new EvaluateException(R.string.error_unclosed_bracket, start);
    }

    nextToken();
    return value;
  }

  protected final T getVariable (String name) {
    return (T)Variables.get(name);
  }

  protected T evaluateElement () throws ExpressionException {
    switch (getTokenType()) {
      case OPEN:
        return evaluateSubexpression();

      case IDENTIFIER: {
        TokenDescriptor token = getCurrentToken();
        String name = getTokenText(token);
        nextToken();

        switch (getTokenType()) {
          case ASSIGN: {
            nextToken();
            T value = evaluateExpression();

            if (!Variables.set(name, value)) {
              throw new EvaluateException(R.string.error_protected_variable, token);
            }

            return value;
          }

          case OPEN: {
            Function function = Functions.get(name);

            if (function == null) {
              throw new EvaluateException(R.string.error_unknown_function, token);
            }

            return (T)function.call(evaluateSubexpression());
          }

          default: {
            T value = getVariable(name);
            if (value != null) return value;
            throw new EvaluateException(R.string.error_unknown_variable, token);
          }
        }
      }

      default:
        throw new EvaluateException(R.string.error_missing_element);
    }
  }

  protected ExpressionEvaluator (String expression) throws ExpressionException {
    super(expression);

    int end = expressionText.length();
    if (getCurrentToken() == null) throw new NoExpressionException(end);

    if (getTokenType() != TokenType.CLOSE) {
      try {
        expressionResult = evaluateExpression();
      } catch (ArithmeticException exception) {
        throw new ExpressionException(
          exception.getMessage(), expressionText.length()
        );
      }

      TokenDescriptor token = getCurrentToken();

      if (token == null) {
        if ((expressionResult != null) && expressionResult.isValid()) return;
        throw new EvaluateException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_missing_operator, token);
      }
    }

    throw new EvaluateException(R.string.error_unopened_bracket);
  }
}
