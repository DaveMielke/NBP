package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;

public class ExpressionEvaluation {
  private final String expressionText;
  private final double expressionResult;

  private static enum TokenType {
    NUMBER,
    IDENTIFIER,
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

  private final List<TokenDescriptor> tokenDescriptors = new ArrayList<TokenDescriptor>();

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
          type = TokenType.MINUS;
          break;

        case '*':
          type = TokenType.TIMES;
          break;

        case '/':
          type = TokenType.DIVIDE;
          break;

        case '^':
          type = TokenType.EXPONENTIATE;
          break;

        default:
          if (Character.isLetter(character)) {
            type = TokenType.IDENTIFIER;

            while (end < length) {
              if (!Character.isLetterOrDigit(expressionText.charAt(end))) break;
              end += 1;
            }
          } else if (Character.isDigit(character)) {
            type = TokenType.NUMBER;

            while (end < length) {
              if (!Character.isDigit(expressionText.charAt(end))) break;
              end += 1;
            }
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

  private final String getTokenText () {
    TokenDescriptor token = getTokenDescriptor();
    return expressionText.substring(token.getStart(), token.getEnd());
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

        case OPEN: {
          int start = getTokenDescriptor().getStart();
          nextToken();
          double value = evaluateExpression();

          if (getTokenType() != TokenType.CLOSE) {
            throw new ExpressionException(R.string.error_unclosed, start);
          }

          nextToken();
          return value;
        }

        case NUMBER: {
          double value = Double.valueOf(getTokenText());
          nextToken();
          return value;
        }

        case IDENTIFIER: {
          String name = getTokenText();
          Double value = Variables.get(name);

          if (value == null) {
            throw new ExpressionException(
              R.string.error_undefined,
              getTokenDescriptor().getStart()
            );
          }

          nextToken();
          return value;
        }

        default: {
          int start = (type == TokenType.END)?
                      expressionText.length():
                      getTokenDescriptor().getStart();

          throw new ExpressionException(R.string.error_missing, start);
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

  public final String getResult () {
    return Double.toString(expressionResult);
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
        throw new ExpressionException(R.string.error_unopened, start);
      }
    }
  }
}
