package org.nbp.calculator;

import java.util.List;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class ExpressionParser {
  protected abstract void parseExpression () throws ExpressionException;
  protected final String expressionText;

  protected static enum TokenType {
    IDENTIFIER,
    ASSIGN,

    OPEN,
    CLOSE,

    DECIMAL,
    HEXADECIMAL,
    DEGREES,

    PLUS,
    MINUS,
    TIMES,
    DIVIDE,
    EXPONENTIATE,
    FACTORIAL,
    PERCENT,

    NOT,
    AND,
    OR,
    XOR,
    LSL,
    LSR,
    ASR,

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

  protected final int findToken (int start, int end) {
    while (start < end) {
      if (!Character.isWhitespace(expressionText.charAt(start))) break;
      start += 1;
    }

    return start;
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

  protected final TokenDescriptor getCurrentToken () {
    if (tokenIndex == tokenCount) return null;
    return tokenDescriptors.get(tokenIndex);
  }

  protected final TokenType getTokenType () {
    TokenDescriptor token = getCurrentToken();
    if (token == null) return TokenType.END;
    return token.getType();
  }

  private final int getTokenStart () {
    TokenDescriptor token = getCurrentToken();
    if (token != null) return token.getStart();
    return expressionText.length();
  }

  protected final String getTokenText (TokenDescriptor token) {
    return expressionText.substring(token.getStart(), token.getEnd());
  }

  protected final String getTokenText () {
    return getTokenText(getCurrentToken());
  }

  protected class EvaluateException extends ExpressionException {
    public EvaluateException (int error, int start) {
      super(error, start);
    }

    public EvaluateException (int error, TokenDescriptor token) {
      this(error, token.getStart());
    }

    public EvaluateException (int error) {
      this(error, getTokenStart());
    }
  }

  protected ExpressionParser (String expression) throws ExpressionException {
    expressionText = expression;
    tokenDescriptors.clear();
    parseExpression();
    tokenCount = tokenDescriptors.size();
  }
}
