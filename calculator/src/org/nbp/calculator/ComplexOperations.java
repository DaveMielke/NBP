package org.nbp.calculator;

public class ComplexOperations extends Operations {
  public ComplexOperations () {
    super();
  }

  @Override
  public Class<? extends Function> getFunctionType () {
    return ComplexFunction.class;
  }

  @Override
  public Class<?> getArgumentType () {
    return ComplexNumber.class;
  }

  @FunctionMethod(
    summary = "real component of complex number"
  )

  public final static ComplexNumber real (ComplexNumber number) {
    return new ComplexNumber(number.real());
  }

  @FunctionMethod(
    summary = "imaginary component of complex number"
  )

  public final static ComplexNumber imag (ComplexNumber number) {
    return new ComplexNumber(number.imag());
  }

  @FunctionMethod(
    summary = "amplitude of complex number"
  )

  public final static ComplexNumber abs (ComplexNumber number) {
    return new ComplexNumber(number.abs());
  }

  @FunctionMethod(
    summary = "phase of complex number"
  )

  public final static ComplexNumber arg (ComplexNumber number) {
    return new ComplexNumber(number.arg());
  }

  @FunctionMethod(
    summary = "negation of complex number"
  )

  public final static ComplexNumber neg (ComplexNumber number) {
    return number.neg();
  }

  @FunctionMethod(
    summary = "conjugate of complex number"
  )

  public final static ComplexNumber cnj (ComplexNumber number) {
    return number.cnj();
  }

  @FunctionMethod(
    summary = "reciprocal of complex number"
  )

  public final static ComplexNumber rcp (ComplexNumber number) {
    return number.rcp();
  }

  @FunctionMethod(
    summary = "natural log of complex number"
  )

  public final static ComplexNumber log (ComplexNumber number) {
    return number.log();
  }

  @FunctionMethod(
    summary = "log base 10 of complex number"
  )

  public final static ComplexNumber log10 (ComplexNumber number) {
    return number.log(10d);
  }

  @FunctionMethod(
    summary = "natural exponential of complex number"
  )

  public final static ComplexNumber exp (ComplexNumber number) {
    return number.exp();
  }

  @FunctionMethod(
    summary = "square root of complex number"
  )

  public final static ComplexNumber sqrt (ComplexNumber number) {
    if (!number.hasImag()) {
      double real = number.real();
      if (real < 0d) return new ComplexNumber(0d, Math.sqrt(-real));
      return new ComplexNumber(Math.sqrt(real));
    }

    return number.pow(new ComplexNumber(0.5d));
  }

  @FunctionMethod(
    summary = "cube root of complex number"
  )

  public final static ComplexNumber cbrt (ComplexNumber number) {
    if (!number.hasImag()) {
      double real = number.real();
      if (real < 0d) return new ComplexNumber(-Math.cbrt(-real));
      return new ComplexNumber(Math.cbrt(real));
    }

    return number.pow(new ComplexNumber(1d / 3d));
  }

  @FunctionMethod(
    summary = "gamma function of complex number"
  )

  public final static ComplexNumber gamma (ComplexNumber number) {
    return number.gamma();
  }
}
