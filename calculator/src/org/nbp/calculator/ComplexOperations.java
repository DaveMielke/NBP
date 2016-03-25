package org.nbp.calculator;

public abstract class ComplexOperations extends ComplexCommon {
  private ComplexOperations () {
  }

  public final static ComplexNumber real (ComplexNumber number) {
    return new ComplexNumber(number.real());
  }

  public final static ComplexNumber imag (ComplexNumber number) {
    return new ComplexNumber(number.imag());
  }

  public final static ComplexNumber abs (ComplexNumber number) {
    return number.abs();
  }

  public final static ComplexNumber neg (ComplexNumber number) {
    return number.neg();
  }

  public final static ComplexNumber con (ComplexNumber number) {
    return number.con();
  }

  public final static ComplexNumber rec (ComplexNumber number) {
    return number.rec();
  }

  public final static ComplexNumber log (ComplexNumber number) {
    return number.log();
  }

  public final static ComplexNumber exp (ComplexNumber number) {
    return number.exp();
  }
}
