package org.nbp.calculator;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class ExpressionParser {
  protected ExpressionParser () {
  }

  protected class ParseException extends ExpressionException {
    public ParseException (int error, int start) {
      super(error, start);
    }
  }

  protected abstract void parseExpression () throws ParseException;
  private String expressionText;

  protected final int getExpressionLength () {
    return expressionText.length();
  }

  protected final char getExpressionCharacter (int index) {
    return expressionText.charAt(index);
  }

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
    MODULO,
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

  protected interface PatternVerifier {
    public boolean verifyPattern (Matcher matcher);
  }

  protected final int findEndOfPattern (
    Pattern pattern, int start, int end, PatternVerifier verifier
  ) {
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

  protected final int findEndOfPattern (
    Pattern pattern, int start, int end, int error
  ) throws ParseException {
    end = findEndOfPattern(pattern, start, end, null);
    if (end > start) return end;
    throw new ParseException(error, start);
  }

  private int tokenCount;
  private int tokenIndex;

  protected final void parseExpression (String expression) throws ParseException {
    expressionText = expression;
    tokenDescriptors.clear();
    parseExpression();
    tokenCount = tokenDescriptors.size();
    tokenIndex = 0;
  }

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

  protected final String getTokenText () {
    TokenDescriptor token = getCurrentToken();
    return expressionText.substring(token.getStart(), token.getEnd());
  }

  private final Stack<TokenDescriptor> tokenStack
          = new Stack<TokenDescriptor>();

  protected final void pushToken (String reason) {
    tokenStack.push(getCurrentToken());
  }

  protected final void popToken () {
    tokenStack.pop();
  }

  private final int getTokenStart () {
    if (!tokenStack.empty()) {
      TokenDescriptor token = tokenStack.peek();
      if (token != null) return token.getStart();
    }

    return expressionText.length();
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

    public EvaluateException (Exception exception) {
      super(exception.getMessage(), getTokenStart());
    }
  }
}
