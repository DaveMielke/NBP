package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class ExpressionEvaluation {
  protected final String expressionText;
  private final ComplexNumber expressionResult;

  protected static enum TokenType {
    IDENTIFIER,
    DECIMAL,
    HEXADECIMAL,
    DEGREES,
    RESULT,

    OPEN,
    CLOSE,

    ASSIGN,
    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    EXPONENTIATE,
    FACTORIAL,
    PERCENT,

    END;
  }

  protected static class TokenDescriptor {
    private final TokenType tokenType;
    private final int tokenStart;
    private final int tokenEnd;

    private TokenDescriptor (TokenType type, int start, int end) {
      tokenType = type;
      tokenStart = start;
      tokenEnd = end;
    }

    public final TokenType getType () {
      return tokenType;
    }

    public final int getStart () {
      return tokenStart;
    }

    public final int getEnd () {
      return tokenEnd;
    }
  }

  private final List<TokenDescriptor> tokenDescriptors
     = new ArrayList<TokenDescriptor>();

  protected final void addToken (TokenType type, int start, int end) {
    tokenDescriptors.add(new TokenDescriptor(type, start, end));
  }

  protected class ParseException extends ExpressionException {
    public ParseException (int error, int start) {
      super(error, start);
    }
  }

  protected interface PatternVerifier {
    public boolean verifyPattern (Matcher matcher);
  }

  protected final int findEndOfPattern (Pattern pattern, int start, int end, PatternVerifier verifier) {
    Matcher matcher = pattern.matcher(expressionText);
    matcher.region(start, end);
    if (matcher.lookingAt()) {
      if ((end = matcher.end()) > start) {
        if (verifier == null) return end;
        if (verifier.verifyPattern(matcher)) return end;
      }
    }

    return start;
  }

  protected final int findEndOfPattern (Pattern pattern, int start, int end, int error) throws ExpressionException {
    end = findEndOfPattern(pattern, start, end, null);
    if (end > start) return end;
    throw new ParseException(error, start);
  }

  private final int tokenCount;
  private int tokenIndex = 0;

  protected final void nextToken () {
    if (tokenIndex < tokenCount) tokenIndex += 1;
  }

  protected final TokenDescriptor getTokenDescriptor () {
    if (tokenIndex == tokenCount) return null;
    return tokenDescriptors.get(tokenIndex);
  }

  protected final TokenType getTokenType () {
    TokenDescriptor token = getTokenDescriptor();
    if (token == null) return TokenType.END;
    return token.getType();
  }

  private final int getTokenStart () {
    TokenDescriptor token = getTokenDescriptor();
    if (token != null) return token.getStart();
    return expressionText.length();
  }

  protected final String getTokenText (TokenDescriptor token) {
    return expressionText.substring(token.getStart(), token.getEnd());
  }

  protected final String getTokenText () {
    return getTokenText(getTokenDescriptor());
  }

  protected class EvaluationException extends ExpressionException {
    public EvaluationException (int error, int start) {
      super(error, start);
    }

    public EvaluationException (int error, TokenDescriptor token) {
      this(error, token.getStart());
    }

    public EvaluationException (int error) {
      this(error, getTokenStart());
    }
  }

  protected final ComplexNumber evaluateSubexpression () throws ExpressionException {
    int start = getTokenDescriptor().getStart();
    nextToken();

    if (getTokenType() == TokenType.CLOSE) {
      throw new EvaluationException(R.string.error_missing_subexpression, start);
    }

    ComplexNumber value = evaluateExpression();

    if (getTokenType() != TokenType.CLOSE) {
      throw new EvaluationException(R.string.error_unclosed_bracket, start);
    }

    nextToken();
    return value;
  }

  public final ComplexNumber getResult () {
    return expressionResult;
  }

  protected abstract void parseExpression () throws ExpressionException;
  protected abstract ComplexNumber evaluateExpression () throws ExpressionException;

  protected ExpressionEvaluation (String expression) throws ExpressionException {
    expressionText = expression;

    tokenDescriptors.clear();
    parseExpression();

    tokenCount = tokenDescriptors.size();
    int end = expressionText.length();

    if (tokenCount == 0) {
      throw new NoExpressionException(end);
    }

    if (getTokenType() != TokenType.CLOSE) {
      expressionResult = evaluateExpression();
      TokenDescriptor token = getTokenDescriptor();

      if (token == null) {
        if (!expressionResult.isNaN()) return;
        throw new EvaluationException(R.string.error_undefined_result, end);
      }

      if (token.getType() != TokenType.CLOSE) {
        throw new EvaluationException(R.string.error_missing_operator, token);
      }
    }

    throw new EvaluationException(R.string.error_unopened_bracket);
  }
}
