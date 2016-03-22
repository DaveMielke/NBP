package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ExpressionEvaluation {
  private final String expressionText;
  private final ComplexNumber expressionResult;

  private static enum TokenType {
    IDENTIFIER,
    DECIMAL,
    HEXADECIMAL,
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

  private final int findEndOfPattern (Pattern pattern, int start, int end, int error) throws ExpressionException {
    Matcher matcher = pattern.matcher(expressionText);
    matcher.region(start, end);
    if (matcher.lookingAt()) {
      end = matcher.end();
      if (end > start) return end;
    }

    throw new ExpressionException(error, start);
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

  private final static String HEXADECIMAL_DIGIT = "[0-9A-Fa-f]";
  private final static Pattern HEXADECIMAL_PATTERN = Pattern.compile(
    HEXADECIMAL_DIGIT + "*"
  + "(\\." + HEXADECIMAL_DIGIT + "+)?"
  + "((?<!^)[Pp][-+]?" + DECIMAL_DIGIT + "+)?"
  );

  private final int findEndOfHexadecimal (int start, int end) throws ExpressionException {
    return findEndOfPattern(HEXADECIMAL_PATTERN, start, end, R.string.error_invalid_hexadecimal);
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
        case '.':
          type = TokenType.DECIMAL;
          end = findEndOfDecimal(start, length);
          break;

        case '#':
          type = TokenType.HEXADECIMAL;
          start += 1;
          end = findEndOfHexadecimal(start, length);
          break;

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

        case ComplexFormatter.SUBTRACTION_SIGN:
        case '-':
          type = TokenType.MINUS;
          break;

        case ComplexFormatter.MULTIPLICATION_SIGN:
        case '*':
          type = TokenType.TIMES;
          break;

        case ComplexFormatter.DIVISION_SIGN:
        case '/':
          type = TokenType.DIVIDE;
          break;

        case '^':
          type = TokenType.EXPONENTIATE;
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
            throw new ExpressionException(R.string.error_unexpected_character, start);
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

  private final ComplexNumber evaluateSubexpression () throws ExpressionException {
    int start = getTokenDescriptor().getStart();
    nextToken();

    if (getTokenType() == TokenType.CLOSE) {
      throw new ExpressionException(R.string.error_missing_subexpression, start);
    }

    ComplexNumber value = evaluateExpression();

    if (getTokenType() != TokenType.CLOSE) {
      throw new ExpressionException(R.string.error_unclosed_bracket, start);
    }

    nextToken();
    return value;
  }

  private final ComplexNumber evaluateElement () throws ExpressionException {
    TokenType type = getTokenType();

    switch (type) {
      case DECIMAL: {
        double value = Double.valueOf(getTokenText());
        nextToken();
        return new ComplexNumber(value);
      }

      case HEXADECIMAL: {
        String text = "0X" + getTokenText().toUpperCase();
        if (text.indexOf('P') < 0) text += "P0";

        nextToken();
        return new ComplexNumber(Double.valueOf(text));
      }

      case OPEN:
        return evaluateSubexpression();

      case RESULT: {
        ComplexNumber value = SavedSettings.getResult();

        if (value.isNaN()) {
          throw new ExpressionException(
            R.string.error_no_result,
            getTokenDescriptor().getStart()
          );
        }

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
            ComplexNumber value = evaluateExpression();

            if (!Variables.set(name, value)) {
              throw new ExpressionException(
                R.string.error_protected_variable,
                token.getStart()
              );
            }

            return value;
          }

          case OPEN: {
            ComplexFunction function = Functions.get(name);

            if (function == null) {
              throw new ExpressionException(R.string.error_unknown_function, token.getStart());
            }

            return function.call(evaluateSubexpression());
          }

          default: {
            ComplexNumber value = Variables.get(name);
            if (value != null) return value;

            throw new ExpressionException(R.string.error_unknown_variable, token.getStart());
          }
        }
      }

      default: {
        int start = (type == TokenType.END)?
                    expressionText.length():
                    getTokenDescriptor().getStart();

        throw new ExpressionException(R.string.error_missing_element, start);
      }
    }
  }

  private final ComplexNumber evaluatePrimary () throws ExpressionException {
    switch (getTokenType()) {
      case PLUS:
        nextToken();
        return evaluatePrimary();

      case MINUS:
        nextToken();
        return evaluatePrimary().neg();

      default:
        return evaluateElement();
    }
  }

  private final ComplexNumber evaluateExponentiations () throws ExpressionException {
    ComplexNumber value = evaluatePrimary();

    while (true) {
      switch (getTokenType()) {
        case EXPONENTIATE:
          nextToken();
          value = value.pow(evaluatePrimary());
          break;

        default:
          return value;
      }
    }
  }

  private final ComplexNumber evaluateProductsAndQuotients () throws ExpressionException {
    ComplexNumber value = evaluateExponentiations();

    while (true) {
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
        case RESULT:
        case IDENTIFIER:
          value = value.mul(evaluateElement());
          break;

        default:
          return value;
      }
    }
  }

  private final ComplexNumber evaluateSumsAndDifferences () throws ExpressionException {
    ComplexNumber value = evaluateProductsAndQuotients();

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

  private final ComplexNumber evaluateExpression () throws ExpressionException {
    ComplexNumber value = evaluateSumsAndDifferences();
    return value;
  }

  public final ComplexNumber getResult () {
    return expressionResult;
  }

  public ExpressionEvaluation (String expression) throws ExpressionException {
    expressionText = expression;
    parseExpression();

    tokenCount = tokenDescriptors.size();
    int end = expressionText.length();

    if (tokenCount == 0) {
      throw new ExpressionException(R.string.error_no_expression, end);
    }

    if (getTokenType() != TokenType.CLOSE) {
      expressionResult = evaluateExpression();
      TokenDescriptor token = getTokenDescriptor();

      if (token == null) {
        if (!expressionResult.isNaN()) return;
        throw new ExpressionException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new ExpressionException(R.string.error_missing_operator, token.getStart());
      }
    }

    throw new ExpressionException(
      R.string.error_unopened_bracket,
      getTokenDescriptor().getStart()
    );
  }
}
