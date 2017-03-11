package org.nbp.calculator;

import java.math.BigInteger;

public abstract class WholeNumber extends GenericNumber {
  public abstract String toPureString ();
  protected abstract WholeNumber newWholeNumber (long value);

  protected final long value;

  protected WholeNumber (long number) {
    value = number;
  }

  public final long getValue () {
    return value;
  }

  public final static long valueOf (String string, int radix) {
    return new BigInteger(string, radix).longValue();
  }

  protected final static long toLong (String string) {
    return Long.decode(string);
  }

  @Override
  public final String format () {
    return new WholeFormatter().format(this);
  }

  @Override
  public final boolean isValid () {
    return true;
  }

  public final WholeNumber not () {
    return newWholeNumber(~value);
  }

  public final WholeNumber and (WholeNumber number) {
    return newWholeNumber(value & number.getValue());
  }

  public final WholeNumber or (WholeNumber number) {
    return newWholeNumber(value | number.getValue());
  }

  public final WholeNumber xor (WholeNumber number) {
    return newWholeNumber(value ^ number.getValue());
  }

  public final WholeNumber lsl (WholeNumber number) {
    return newWholeNumber(value << number.getValue());
  }

  public final WholeNumber lsr (WholeNumber number) {
    return newWholeNumber(value >>> number.getValue());
  }

  public final WholeNumber asr (WholeNumber number) {
    return newWholeNumber(value >> number.getValue());
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

  public final WholeNumber mod (WholeNumber number) {
    return newWholeNumber(value % number.getValue());
  }
}
