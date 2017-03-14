package org.nbp.calculator;

public abstract class WholeEvaluator extends ExpressionEvaluator<WholeNumber> {
  private final int digitRadix;

  public WholeEvaluator (int radix) {
    super();
    digitRadix = radix;
  }

  private final boolean isDigit (char character) {
    return Character.digit(character, digitRadix) >= 0;
  }

  @Override
  protected final void parseExpression () throws ParseException {
    final int length = getExpressionLength();
    int end = 0;

    while (true) {
      int start = findToken(end, length);
      if (start == length) return;

      char character = getExpressionCharacter(start);
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

        case Formatter.SUBTRACTION_SIGN:
        case '-':
          type = TokenType.MINUS;
          break;

        case Formatter.MULTIPLICATION_SIGN:
        case '*':
          type = TokenType.TIMES;
          break;

        case Formatter.DIVISION_SIGN:
        case '/':
          type = TokenType.DIVIDE;
          break;

        case '%':
          type = TokenType.MODULO;
          break;

        case '~':
          type = TokenType.NOT;
          break;

        case '&':
          type = TokenType.AND;
          break;

        case '|':
          type = TokenType.OR;
          break;

        case '^':
          type = TokenType.XOR;
          break;

        case '<':
          type = TokenType.LSL;
          break;

        case '>':
          type = TokenType.LSR;
          break;

        default: {
          if (isIdentifierCharacter(character, true)) {
            type = TokenType.IDENTIFIER;

            while (end < length) {
              if (!isIdentifierCharacter(getExpressionCharacter(end), false)) break;
              end += 1;
            }
          } else if (isDigit(character)) {
            type = TokenType.HEXADECIMAL;

            while (end < length) {
              if (!isDigit(getExpressionCharacter(end))) break;
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

  private WholeNumber evaluateElement () throws ExpressionException {
    pushToken("whole element");

    try {
      switch (getTokenType()) {
        case HEXADECIMAL: {
          String text = getTokenText().toUpperCase();
          nextToken();
          return new HexadecimalNumber(WholeNumber.valueOf(text, HexadecimalNumber.RADIX));
        }

        default:
          return evaluateElement(WholeNumber.class);
      }
    } finally {
      popToken();
    }
  }

  private final WholeNumber evaluatePrimary () throws ExpressionException {
    pushToken("whole primary");

    try {
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

        default:
          return evaluateElement();
      }
    } finally {
      popToken();
    }
  }

  private final WholeNumber evaluateProductsAndQuotients () throws ExpressionException {
    WholeNumber value = evaluatePrimary();

    while (true) {
      pushToken("whole products/quotients");

      try {
        try {
          switch (getTokenType()) {
            case TIMES:
              nextToken();
              value = value.mul(evaluatePrimary());
              break;

            case DIVIDE:
              nextToken();
              value = value.div(evaluatePrimary());
              break;

            case MODULO:
              nextToken();
              value = value.mod(evaluatePrimary());
              break;

            default:
              return value;
          }
        } catch (ArithmeticException exception) {
          throw new EvaluateException(exception);
        }
      } finally {
        popToken();
      }
    }
  }

  private final WholeNumber evaluateSumsAndDifferences () throws ExpressionException {
    WholeNumber value = evaluateProductsAndQuotients();

    while (true) {
      pushToken("whole sums/differences");

      try {
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
      } finally {
        popToken();
      }
    }
  }

  private final WholeNumber evaluateShiftOperations () throws ExpressionException {
    WholeNumber value = evaluateSumsAndDifferences();

    while (true) {
      pushToken("whole shifts");

      try {
        switch (getTokenType()) {
          case LSL:
            nextToken();
            value = value.lsl(evaluateSumsAndDifferences());
            break;

          case LSR:
            nextToken();
            value = value.lsr(evaluateSumsAndDifferences());
            break;

          case ASR:
            nextToken();
            value = value.asr(evaluateSumsAndDifferences());
            break;

          default:
            return value;
        }
      } finally {
        popToken();
      }
    }
  }

  private final WholeNumber evaluateBitwiseOperations () throws ExpressionException {
    WholeNumber value = evaluateShiftOperations();

    while (true) {
      pushToken("whole bitwise");

      try {
        switch (getTokenType()) {
          case AND:
            nextToken();
            value = value.and(evaluateShiftOperations());
            break;

          case OR:
            nextToken();
            value = value.or(evaluateShiftOperations());
            break;

          case XOR:
            nextToken();
            value = value.xor(evaluateShiftOperations());
            break;

          default:
            return value;
        }
      } finally {
        popToken();
      }
    }
  }

  @Override
  protected final WholeNumber evaluateExpression () throws ExpressionException {
    return evaluateBitwiseOperations();
  }
}
