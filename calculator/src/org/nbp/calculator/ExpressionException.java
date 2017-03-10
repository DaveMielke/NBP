package org.nbp.calculator;

import org.nbp.common.CommonContext;

public class ExpressionException extends Exception {
  private final int expressionLocation;

  public final int getLocation () {
    return expressionLocation;
  }

  public ExpressionException (String message, int location) {
    super(message);
    expressionLocation = location;
  }

  public ExpressionException (int message, int location) {
    this(CommonContext.getString(message), location);
  }
}
