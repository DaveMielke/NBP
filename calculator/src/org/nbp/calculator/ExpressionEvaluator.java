package org.nbp.calculator;

public abstract class ExpressionEvaluator<T extends GenericNumber> extends ExpressionParser {
  protected abstract T evaluateExpression () throws ExpressionException;
  private final T expressionResult;

  public final T getResult () {
    return expressionResult;
  }

  protected final T evaluateSubexpression () throws ExpressionException {
    pushToken("generic subexpression");
    nextToken();

    try {
      if (getTokenType() == TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_missing_subexpression);
      }

      T value = evaluateExpression();

      if (getTokenType() != TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_unclosed_bracket);
      }

      nextToken();
      return value;
    } finally {
      popToken();
    }
  }

  protected final T evaluateElement (Class<T> type) throws ExpressionException {
    pushToken("generic element");

    try {
      switch (getTokenType()) {
        case OPEN:
          return evaluateSubexpression();

        case IDENTIFIER: {
          String name = getTokenText();
          nextToken();

          switch (getTokenType()) {
            case ASSIGN: {
              nextToken();
              T value = evaluateExpression();

              if (!Variables.set(name, value)) {
                throw new EvaluateException(R.string.error_protected_variable);
              }

              return value;
            }

            case OPEN: {
              Function function = Functions.get(name);

              if (function == null) {
                throw new EvaluateException(R.string.error_unknown_function);
              }

              Object value = function.call(evaluateSubexpression());
              if (value != null) return (T)value;
              throw new EvaluateException(R.string.error_incompatible_function);
            }

            default: {
              Object value;

              try {
                value = (T)Variables.get(name);
              } catch (NumberFormatException exception) {
                throw new EvaluateException(R.string.error_incompatible_variable);
              }

              if (value == null) {
                throw new EvaluateException(R.string.error_unknown_variable);
              }

              if (!type.isAssignableFrom(value.getClass())) {
                throw new EvaluateException(R.string.error_incompatible_variable);
              }

              return (T)value;
            }
          }
        }

        default:
          throw new EvaluateException(R.string.error_missing_element);
      }
    } finally {
      popToken();
    }
  }

  protected ExpressionEvaluator (String expression) throws ExpressionException {
    super(expression);

    int end = expressionText.length();
    if (getCurrentToken() == null) throw new NoExpressionException(end);

    if (getTokenType() != TokenType.CLOSE) {
      expressionResult = evaluateExpression();
      TokenDescriptor token = getCurrentToken();

      if (token == null) {
        if ((expressionResult != null) && expressionResult.isValid()) return;
        throw new EvaluateException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_missing_operator, token);
      }
    }

    throw new EvaluateException(R.string.error_unopened_bracket, getCurrentToken());
  }
}
