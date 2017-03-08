package org.nbp.calculator;

public abstract class WholeEvaluator extends ExpressionEvaluator<WholeNumber> {
  protected abstract int getRadix ();

  public WholeEvaluator (String expression) throws ExpressionException {
    super(expression);
  }

  private final boolean isDigit (char character) {
    return Character.digit(character, getRadix()) >= 0;
  }

  @Override
  protected final void parseExpression () throws ExpressionException {
    final int length = expressionText.length();
    int end = 0;

    while (true) {
      int start = findToken(end, length);
      if (start == length) return;

      char character = expressionText.charAt(start);
      end = start + 1;
      TokenType type;

      switch (character) {
        case Function.ARGUMENT_PREFIX:
          type = TokenType.OPEN;
          break;

        case Function.ARGUMENT_SUFFIX:
          type = TokenType.CLOSE;
          break;

        case '=':
          type = TokenType.ASSIGN;
          break;

        case '+':
          type = TokenType.PLUS;
          break;

        case GenericFormatter.SUBTRACTION_SIGN:
        case '-':
          type = TokenType.MINUS;
          break;

        case GenericFormatter.MULTIPLICATION_SIGN:
        case '*':
          type = TokenType.TIMES;
          break;

        case GenericFormatter.DIVISION_SIGN:
        case '/':
          type = TokenType.DIVIDE;
          break;

        case '~':
          type = TokenType.NOT;
          break;

        case '&':
          type = TokenType.AND;
          break;

        case '|':
          type = TokenType.IOR;
          break;

        case '^':
          type = TokenType.XOR;
          break;

        case '<':
          type = TokenType.LSL;
          break;

        case '>':
          type = TokenType.RSL;
          break;

        default: {
          if (Variables.isNameCharacter(character, true)) {
            type = TokenType.IDENTIFIER;

            while (end < length) {
              if (!Variables.isNameCharacter(expressionText.charAt(end), false)) break;
              end += 1;
            }
          } else if (isDigit(character)) {
            type = TokenType.HEXADECIMAL;

            while (end < length) {
              if (!isDigit(expressionText.charAt(end))) break;
              end += 1;
            }
          } else {
            throw new ParseException(R.string.error_unexpected_character, start);
          }

          break;
        }
      }

      addToken(type, start, end);
    }
  }

  @Override
  protected WholeNumber evaluateElement () throws ExpressionException {
    switch (getTokenType()) {
      case HEXADECIMAL: {
        String text = getTokenText().toUpperCase();
        nextToken();
        return new HexadecimalNumber(text);
      }

      default:
        return super.evaluateElement();
    }
  }

  private final WholeNumber evaluatePrimary () throws ExpressionException {
    switch (getTokenType()) {
      case PLUS:
        nextToken();
        return evaluatePrimary();

      case MINUS:
        nextToken();
        return evaluatePrimary().neg();

      case NOT:
        nextToken();
        return evaluatePrimary().not();

      default: {
        return evaluateElement();
      }
    }
  }

  private final WholeNumber evaluateProductsAndQuotients () throws ExpressionException {
    WholeNumber value = evaluatePrimary();

    while (true) {
      switch (getTokenType()) {
        case TIMES:
          nextToken();
          value = value.mul(evaluatePrimary());
          break;

        case DIVIDE:
          nextToken();
          value = value.div(evaluatePrimary());
          break;

        default:
          return value;
      }
    }
  }

  private final WholeNumber evaluateSumsAndDifferences () throws ExpressionException {
    WholeNumber value = evaluateProductsAndQuotients();

    while (true) {
      switch (getTokenType()) {
        case PLUS:
          nextToken();
          value = value.add(evaluateProductsAndQuotients());
          break;

        case MINUS:
          nextToken();
          value = value.sub(evaluateProductsAndQuotients());
          break;

        default:
          return value;
      }
    }
  }

  private final WholeNumber evaluateShiftOperations () throws ExpressionException {
    WholeNumber value = evaluateSumsAndDifferences();

    while (true) {
      switch (getTokenType()) {
        case LSL:
          nextToken();
          value = value.lsl(evaluateSumsAndDifferences());
          break;

        case RSL:
          nextToken();
          value = value.rsl(evaluateSumsAndDifferences());
          break;

        default:
          return value;
      }
    }
  }

  private final WholeNumber evaluateSetOperations () throws ExpressionException {
    WholeNumber value = evaluateShiftOperations();

    while (true) {
      switch (getTokenType()) {
        case AND:
          nextToken();
          value = value.and(evaluateShiftOperations());
          break;

        case IOR:
          nextToken();
          value = value.ior(evaluateShiftOperations());
          break;

        case XOR:
          nextToken();
          value = value.xor(evaluateShiftOperations());
          break;

        default:
          return value;
      }
    }
  }

  @Override
  protected final WholeNumber evaluateExpression () throws ExpressionException {
    return evaluateSetOperations();
  }
}
