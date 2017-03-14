package org.nbp.calculator;

public abstract class ExpressionEvaluator<T extends AbstractNumber> extends ExpressionParser {
  protected abstract T evaluateExpression () throws ExpressionException;

  private T currentResult;
  private int bracketLevel;

  public final AbstractNumber getResult () {
    return currentResult;
  }

  public final int getLevel () {
    return bracketLevel;
  }

  private final T evaluateResult () throws ExpressionException {
    return currentResult = evaluateExpression();
  }

  protected final T evaluateSubexpression () throws ExpressionException {
    pushToken("generic subexpression");
    nextToken();

    try {
      bracketLevel += 1;
      currentResult = null;

      if (getTokenType() == TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_missing_subexpression);
      }

      T value = evaluateResult();

      if (getTokenType() != TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_unclosed_bracket);
      }

      currentResult = null;
      bracketLevel -= 1;

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
              T value = evaluateResult();

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
              Variable variable = Variables.get(name);

              if (variable == null) {
                throw new EvaluateException(R.string.error_unknown_variable);
              }

              T value = (T)variable.getValue(type);
              if (value != null) return value;
              throw new EvaluateException(R.string.error_incompatible_variable);
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

  public final void evaluateExpression (String expression) throws ExpressionException {
    currentResult = null;
    bracketLevel = 0;

    parseExpression(expression);
    int end = getExpressionLength();
    if (getCurrentToken() == null) throw new NoExpressionException(end);

    if (getTokenType() != TokenType.CLOSE) {
      evaluateResult();
      TokenDescriptor token = getCurrentToken();

      if (token == null) {
        if ((currentResult != null) && currentResult.isValid()) return;
        throw new EvaluateException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new EvaluateException(R.string.error_missing_operator, token);
      }
    }

    throw new EvaluateException(R.string.error_unopened_bracket, getCurrentToken());
  }

  protected ExpressionEvaluator () {
    super();
  }
}
