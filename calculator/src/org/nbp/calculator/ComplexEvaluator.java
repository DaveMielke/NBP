package org.nbp.calculator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComplexEvaluator extends ExpressionEvaluator<ComplexNumber> {
  public ComplexEvaluator (String expression) throws ExpressionException {
    super(expression);
  }

  private final static String DECIMAL_DIGIT = "[0-9]";
  private final static Pattern DECIMAL_PATTERN = Pattern.compile(
    DECIMAL_DIGIT + "*"
  + "(\\." + DECIMAL_DIGIT + "+)?"
  + "((?<!^)[Ee][-+]?" + DECIMAL_DIGIT + "+)?"
  );

  private final int findEndOfDecimal (int start, int end) throws ExpressionException {
    return findEndOfPattern(DECIMAL_PATTERN, start, end, R.string.error_invalid_decimal);
  }

  private final static char DEGREE_MINUTES = '"';
  private final static char DEGREE_SECONDS = '\'';
  private final static String DEGREE_FRACTION = "[1-5]?" + DECIMAL_DIGIT;

  private final static Pattern DEGREES_PATTERN = Pattern.compile(
    "(" + DECIMAL_DIGIT + "+)"
  + "(\\" + DEGREE_MINUTES + DEGREE_FRACTION + ")?"
  + "(\\" + DEGREE_SECONDS + DEGREE_FRACTION + ")?"
  );

  private final int findEndOfDegrees (int start, int end) {
    return findEndOfPattern(
      DEGREES_PATTERN, start, end,
      new PatternVerifier() {
        int groupCount = 0;

        private final boolean verifyGroup (Matcher matcher, int group) {
          int start = matcher.start(group);
          int end = matcher.end(group);
          if (end == start) return true;
          groupCount += 1;
          return (matcher.end(group) - matcher.start(group)) > 1;
        }

        @Override
        public boolean verifyPattern (Matcher matcher) {
          return verifyGroup(matcher, 2)
              && verifyGroup(matcher, 3)
              && (groupCount > 0);
        }
      }
    );
  }

  private final static String HEXADECIMAL_DIGIT = "[0-9A-Fa-f]";
  private final static Pattern HEXADECIMAL_PATTERN = Pattern.compile(
    HEXADECIMAL_DIGIT + "*"
  + "(\\." + HEXADECIMAL_DIGIT + "+)?"
  + "((?<!^)[Pp][-+]?" + DECIMAL_DIGIT + "+)?"
  );

  private final int findEndOfHexadecimal (int start, int end) throws ExpressionException {
    return findEndOfPattern(HEXADECIMAL_PATTERN, start, end, R.string.error_invalid_hexadecimal);
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
        case '.':
          type = TokenType.DECIMAL;
          end = findEndOfDecimal(start, length);
          break;

        case '#':
          type = TokenType.HEXADECIMAL;
          start += 1;
          end = findEndOfHexadecimal(start, length);
          break;

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

        case '^':
          type = TokenType.EXPONENTIATE;
          break;

        case '!':
          type = TokenType.FACTORIAL;
          break;

        case '%':
          type = TokenType.PERCENT;
          break;

        default: {
          if (Variables.isNameCharacter(character, true)) {
            type = TokenType.IDENTIFIER;

            while (end < length) {
              if (!Variables.isNameCharacter(expressionText.charAt(end), false)) break;
              end += 1;
            }
          } else if (Character.isDigit(character)) {
            if ((end = findEndOfDegrees(start, length)) > start) {
              type = TokenType.DEGREES;
            } else {
              type = TokenType.DECIMAL;
              end = findEndOfDecimal(start, length);
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

  private final ComplexNumber evaluateElement () throws ExpressionException {
    pushToken("complex element");

    try {
      switch (getTokenType()) {
        case DECIMAL: {
          String text = getTokenText();
          nextToken();
          return new ComplexNumber(text);
        }

        case HEXADECIMAL: {
          String text = "0X" + getTokenText().toUpperCase();
          if (text.indexOf('P') < 0) text += "P0";

          nextToken();
          return new ComplexNumber(text);
        }

        case DEGREES: {
          String text = getTokenText();
          double degrees = 0d;

          {
            int index = text.indexOf(DEGREE_SECONDS);

            if (index >= 0) {
              degrees += Double.valueOf(text.substring(index+1)) / 3600d;
              text = text.substring(0, index);
            }
          }

          {
            int index = text.indexOf(DEGREE_MINUTES);

            if (index >= 0) {
              degrees += Double.valueOf(text.substring(index+1)) / 60d;
              text = text.substring(0, index);
            }
          }

          if (text.length() > 0) {
            degrees += Double.valueOf(text);
          }

          nextToken();
          return new ComplexNumber(degrees);
        }

        default:
          return evaluateElement(ComplexNumber.class);
      }
    } finally {
      popToken();
    }
  }

  private final ComplexNumber evaluatePrimary () throws ExpressionException {
    pushToken("complex primary");

    try {
      switch (getTokenType()) {
        case PLUS:
          nextToken();
          return evaluatePrimary();

        case MINUS:
          nextToken();
          return evaluatePrimary().neg();

        default: {
          ComplexNumber value = evaluateElement();

          while (true) {
            pushToken("complex factorial");

            try {
              switch (getTokenType()) {
                case FACTORIAL:
                  value = value.add(1d).gamma();
                  break;

                default:
                  return value;
              }

              nextToken();
            } finally {
              popToken();
            }
          }
        }
      }
    } finally {
      popToken();
    }
  }

  private final ComplexNumber evaluateExponentiations () throws ExpressionException {
    ComplexNumber value = evaluatePrimary();

    while (true) {
      pushToken("complex exponentiation");

      try {
        switch (getTokenType()) {
          case EXPONENTIATE:
            nextToken();
            value = value.pow(evaluatePrimary());
            break;

          default:
            return value;
        }
      } finally {
        popToken();
      }
    }
  }

  private final ComplexNumber evaluateProductsAndQuotients () throws ExpressionException {
    ComplexNumber value = evaluateExponentiations();

    while (true) {
      pushToken("complex products/quotients");

      try {
        switch (getTokenType()) {
          case TIMES:
            nextToken();
            value = value.mul(evaluateExponentiations());
            break;

          case DIVIDE:
            nextToken();
            value = value.div(evaluateExponentiations());
            break;

          case OPEN:
          case IDENTIFIER:
            value = value.mul(evaluateElement());
            break;

          default:
            return value;
        }
      } finally {
        popToken();
      }
    }
  }

  private final ComplexNumber evaluateSumsAndDifferences () throws ExpressionException {
    ComplexNumber value = evaluateProductsAndQuotients();

    while (true) {
      ComplexNumber operand;
      boolean add;
      pushToken("complex sums/differences");

      try {
        switch (getTokenType()) {
          case PLUS:
            nextToken();
            operand = evaluateProductsAndQuotients();
            add = true;
            break;

          case MINUS:
            nextToken();
            operand = evaluateProductsAndQuotients();
            add = false;
            break;

          default:
            return value;
        }

        if (getTokenType() == TokenType.PERCENT) {
          pushToken("complex percent");

          try {
            nextToken();
            operand = operand.mul(value).div(100);
          } finally {
            popToken();
          }
        }

        if (add) {
          value = value.add(operand);
        } else {
          value = value.sub(operand);
        }
      } finally {
        popToken();
      }
    }
  }

  @Override
  protected final ComplexNumber evaluateExpression () throws ExpressionException {
    return evaluateSumsAndDifferences();
  }
}
