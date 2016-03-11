package org.nbp.calculator;

import org.nbp.common.CommonContext;

public class ExpressionException extends Exception {
  private final int expressionLocation;

  public final int getLocation () {
    return expressionLocation;
  }

  public ExpressionException (int message, int location) {
    super(CommonContext.getString(message));
    expressionLocation = location;
  }
}
