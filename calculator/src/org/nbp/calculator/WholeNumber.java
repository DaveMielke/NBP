package org.nbp.calculator;

public abstract class WholeNumber extends GenericNumber {
  protected abstract WholeNumber newWholeNumber (long value);
  protected abstract WholeFormatter newWholeFormatter ();

  private final long value;

  public WholeNumber (long number) {
    value = number;
  }

  public WholeNumber (String number, int radix) {
    this(Long.valueOf(number, radix));
  }

  public final long getValue () {
    return value;
  }

  public final WholeNumber not () {
    return newWholeNumber(~value);
  }

  public final WholeNumber and (WholeNumber number) {
    return newWholeNumber(value & number.getValue());
  }

  public final WholeNumber ior (WholeNumber number) {
    return newWholeNumber(value | number.getValue());
  }

  public final WholeNumber xor (WholeNumber number) {
    return newWholeNumber(value ^ number.getValue());
  }

  public final WholeNumber lsl (WholeNumber number) {
    return newWholeNumber(value << number.getValue());
  }

  public final WholeNumber rsl (WholeNumber number) {
    return newWholeNumber(value >>> number.getValue());
  }

  public final WholeNumber neg () {
    return newWholeNumber(-value);
  }

  public final WholeNumber add (WholeNumber number) {
    return newWholeNumber(value + number.getValue());
  }

  public final WholeNumber sub (WholeNumber number) {
    return newWholeNumber(value - number.getValue());
  }

  public final WholeNumber mul (WholeNumber number) {
    return newWholeNumber(value * number.getValue());
  }

  public final WholeNumber div (WholeNumber number) {
    return newWholeNumber(value / number.getValue());
  }

  @Override
  public final boolean isValid () {
    return true;
  }

  @Override
  public final String format () {
    return newWholeFormatter().format(value);
  }
}
