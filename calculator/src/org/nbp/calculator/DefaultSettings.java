package org.nbp.calculator;

public abstract class DefaultSettings {
  public final static CalculatorMode CALCULATOR_MODE = CalculatorMode.DECIMAL;
  public final static DecimalNotation DECIMAL_NOTATION = DecimalNotation.FIXED;
  public final static AngleUnit ANGLE_UNIT = AngleUnit.DEGREES;

  private DefaultSettings () {
  }
}
