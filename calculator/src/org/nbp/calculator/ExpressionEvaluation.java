package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ExpressionEvaluation {
  private final String expressionText;
  private final double expressionResult;

  private static enum TokenType {
    DECIMAL,
    HEXADECIMAL,
    IDENTIFIER,
    RESULT,
    OPEN,
    CLOSE,
    ASSIGN,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    EXPONENTIATE,
    END;
  }

  private static class TokenDescriptor {
    private final TokenType tokenType;
    private final int tokenStart;
    private final int tokenEnd;

    public final TokenType getType () {
      return tokenType;
    }

    public final int getStart () {
      return tokenStart;
    }

    public final int getEnd () {
      return tokenEnd;
    }

    public TokenDescriptor (TokenType type, int start, int end) {
      tokenType = type;
      tokenStart = start;
      tokenEnd = end;
    }
  }

  private final List<TokenDescriptor> tokenDescriptors
     = new ArrayList<TokenDescriptor>();

  private final int findEndOfPattern (Pattern pattern, int start, int end) throws ExpressionException {
    Matcher matcher = pattern.matcher(expressionText);
    matcher.region(start, end);
    if (matcher.lookingAt()) return matcher.end();
    throw new ExpressionException(R.string.error_value, start);
  }

  private final static Pattern DECIMAL_PATTERN = Pattern.compile(
    "\\d*(\\.\\d+)?([eE][-+]?\\d+)?"
  );

  private final int findEndOfDecimal (int start, int end) throws ExpressionException {
    return findEndOfPattern(DECIMAL_PATTERN, start, end);
  }

  private final static Pattern HEXADECIMAL_PATTERN = Pattern.compile(
    "#[\\da-fA-F]+"
  );

  private final int findEndOfHexadecimal (int start, int end) throws ExpressionException {
    return findEndOfPattern(HEXADECIMAL_PATTERN, start, end);
  }

  private final void parseExpression () throws ExpressionException {
    tokenDescriptors.clear();

    int length = expressionText.length();
    int end = 0;

    while (true) {
      int start = end;
      char character;

      while (true) {
        if (start == length) return;
        character = expressionText.charAt(start);

        if (!Character.isWhitespace(character)) break;
        start += 1;
      }

      end = start + 1;
      TokenType type;

      switch (character) {
        case '$':
          type = TokenType.RESULT;
          break;

        case '(':
          type = TokenType.OPEN;
          break;

        case ')':
          type = TokenType.CLOSE;
          break;

        case '=':
          type = TokenType.ASSIGN;
          break;

        case '+':
          type = TokenType.PLUS;
          break;

        case '-':
        case '−':
          type = TokenType.MINUS;
          break;

        case '*':
        case '×':
          type = TokenType.TIMES;
          break;

        case '/':
        case '÷':
          type = TokenType.DIVIDE;
          break;

        case '^':
          type = TokenType.EXPONENTIATE;
          break;

        case '.':
          type = TokenType.DECIMAL;
          end = findEndOfDecimal(start, length);
          break;

        case '#':
          type = TokenType.HEXADECIMAL;
          end = findEndOfHexadecimal(start, length);
          break;

        default:
          if (Variables.isNameCharacter(character, true)) {
            type = TokenType.IDENTIFIER;

            while (end < length) {
              if (!Variables.isNameCharacter(expressionText.charAt(end), false)) break;
              end += 1;
            }
          } else if (Character.isDigit(character)) {
            type = TokenType.DECIMAL;
            end = findEndOfDecimal(start, length);
          } else {
            throw new ExpressionException(R.string.error_syntax, start);
          }
      }

      tokenDescriptors.add(new TokenDescriptor(type, start, end));
    }
  }

  private final int tokenCount;
  private int tokenIndex = 0;

  private final void nextToken () {
    if (tokenIndex < tokenCount) tokenIndex += 1;
  }

  private final TokenDescriptor getTokenDescriptor () {
    if (tokenIndex == tokenCount) return null;
    return tokenDescriptors.get(tokenIndex);
  }

  private final TokenType getTokenType () {
    TokenDescriptor token = getTokenDescriptor();
    if (token == null) return TokenType.END;
    return token.getType();
  }

  private final String getTokenText (TokenDescriptor token) {
    return expressionText.substring(token.getStart(), token.getEnd());
  }

  private final String getTokenText () {
    return getTokenText(getTokenDescriptor());
  }

  private final double evaluateSubexpression () throws ExpressionException {
    int start = getTokenDescriptor().getStart();
    nextToken();
    double value = evaluateExpression();

    if (getTokenType() != TokenType.CLOSE) {
      throw new ExpressionException(R.string.error_unclosed_bracket, start);
    }

    nextToken();
    return value;
  }

  private final double evaluateTerm () throws ExpressionException {
    while (true) {
      TokenType type = getTokenType();

      switch (type) {
        case PLUS:
          nextToken();
          return evaluateTerm();

        case MINUS:
          nextToken();
          return -evaluateTerm();

        case OPEN:
          return evaluateSubexpression();

        case RESULT: {
          Double value = Variables.get(Variables.RESULT);

          if (value == null) {
            throw new ExpressionException(
              R.string.error_no_result,
              getTokenDescriptor().getStart()
            );
          }

          nextToken();
          return value;
        }

        case DECIMAL: {
          double value = Double.valueOf(getTokenText());
          nextToken();
          return value;
        }

        case HEXADECIMAL: {
          double value = Double.valueOf(
            ("0X" + getTokenText().substring(1) + "P0")
          );

          nextToken();
          return value;
        }

        case IDENTIFIER: {
          TokenDescriptor token = getTokenDescriptor();
          String name = getTokenText(token);
          nextToken();

          switch (getTokenType()) {
            case ASSIGN: {
              nextToken();
              double value = evaluateExpression();

              if (!Variables.set(name, value)) {
                throw new ExpressionException(
                  R.string.error_protected_variable,
                  token.getStart()
                );
              }

              return value;
            }

            case OPEN: {
              Function function = Functions.get(name);

              if (function == null) {
                throw new ExpressionException(R.string.error_unknown_function, token.getStart());
              }

              return function.call(evaluateSubexpression());
            }

            default: {
              Double value = Variables.get(name);
              if (value != null) return value;

              throw new ExpressionException(R.string.error_unknown_variable, token.getStart());
            }
          }
        }

        default: {
          int start = (type == TokenType.END)?
                      expressionText.length():
                      getTokenDescriptor().getStart();

          throw new ExpressionException(R.string.error_missing_term, start);
        }
      }
    }
  }

  private final double evaluateExponentiations () throws ExpressionException {
    double value = evaluateTerm();

    while (true) {
      switch (getTokenType()) {
        case EXPONENTIATE:
          nextToken();
          value = Math.pow(value, evaluateTerm());
          break;

        default:
          return value;
      }
    }
  }

  private final double evaluateProductsAndQuotients () throws ExpressionException {
    double value = evaluateExponentiations();

    while (true) {
      switch (getTokenType()) {
        case TIMES:
          nextToken();
          value *= evaluateExponentiations();
          break;

        case DIVIDE:
          nextToken();
          value /= evaluateExponentiations();
          break;

        default:
          return value;
      }
    }
  }

  private final double evaluateSumsAndDifferences () throws ExpressionException {
    double value = evaluateProductsAndQuotients();

    while (true) {
      switch (getTokenType()) {
        case PLUS:
          nextToken();
          value += evaluateProductsAndQuotients();
          break;

        case MINUS:
          nextToken();
          value -= evaluateProductsAndQuotients();
          break;

        default:
          return value;
      }
    }
  }

  private final double evaluateExpression () throws ExpressionException {
    double value = evaluateSumsAndDifferences();
    return value;
  }

  public final double getResult () {
    return expressionResult;
  }

  public ExpressionEvaluation (String expression) throws ExpressionException {
    expressionText = expression;
    parseExpression();

    tokenCount = tokenDescriptors.size();
    expressionResult = evaluateExpression();
    TokenDescriptor token = getTokenDescriptor();

    if (token != null) {
      TokenType type = token.getType();
      int start = token.getStart();

      if (type == TokenType.CLOSE) {
        throw new ExpressionException(R.string.error_unopened_bracket, start);
      }
    }

    if (Double.isNaN(expressionResult)) {
      throw new ExpressionException(R.string.error_undefined_result, expressionText.length());
    }
  }
}
