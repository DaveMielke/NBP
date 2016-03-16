package org.nbp.calculator;

public abstract class CalculatorSettings {
  public volatile static Double RESULT = null;
  public volatile static boolean DEGREES = true;

  private CalculatorSettings () {
  }
}
