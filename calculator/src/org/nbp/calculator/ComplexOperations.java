package org.nbp.calculator;

public class ComplexOperations extends Operations {
  public ComplexOperations () {
    super();
  }

  @Override
  public Class<? extends ComplexFunction> getFunctionType () {
    return ComplexFunction.class;
  }

  @Override
  public Class<?> getArgumentType () {
    return ComplexNumber.class;
  }

  public final static ComplexNumber real (ComplexNumber number) {
    return new ComplexNumber(number.real());
  }

  public final static ComplexNumber imag (ComplexNumber number) {
    return new ComplexNumber(number.imag());
  }

  public final static ComplexNumber abs (ComplexNumber number) {
    return new ComplexNumber(number.abs());
  }

  public final static ComplexNumber arg (ComplexNumber number) {
    return new ComplexNumber(number.arg());
  }

  public final static ComplexNumber neg (ComplexNumber number) {
    return number.neg();
  }

  public final static ComplexNumber cnj (ComplexNumber number) {
    return number.cnj();
  }

  public final static ComplexNumber rcp (ComplexNumber number) {
    return number.rcp();
  }

  public final static ComplexNumber log (ComplexNumber number) {
    return number.log();
  }

  public final static ComplexNumber log10 (ComplexNumber number) {
    return number.log(10d);
  }

  public final static ComplexNumber exp (ComplexNumber number) {
    return number.exp();
  }

  public final static ComplexNumber sqrt (ComplexNumber number) {
    return number.pow(new ComplexNumber(0.5d));
  }

  public final static ComplexNumber cbrt (ComplexNumber number) {
    return number.pow(new ComplexNumber(1d / 3d));
  }
}
