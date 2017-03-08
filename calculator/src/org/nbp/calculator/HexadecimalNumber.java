package org.nbp.calculator;

public class HexadecimalNumber extends GenericNumber {
  private final long value;

  public HexadecimalNumber (long x) {
    value = x;
  }

  public HexadecimalNumber (String n) {
    this(Long.valueOf(n, 0X10));
  }

  public final long getValue () {
    return value;
  }

  public final HexadecimalNumber not () {
    return new HexadecimalNumber(~value);
  }

  public final HexadecimalNumber and (HexadecimalNumber number) {
    return new HexadecimalNumber(value & number.getValue());
  }

  public final HexadecimalNumber ior (HexadecimalNumber number) {
    return new HexadecimalNumber(value | number.getValue());
  }

  public final HexadecimalNumber xor (HexadecimalNumber number) {
    return new HexadecimalNumber(value ^ number.getValue());
  }

  public final HexadecimalNumber lsl (HexadecimalNumber number) {
    return new HexadecimalNumber(value << number.getValue());
  }

  public final HexadecimalNumber rsl (HexadecimalNumber number) {
    return new HexadecimalNumber(value >>> number.getValue());
  }

  public final HexadecimalNumber neg () {
    return new HexadecimalNumber(-value);
  }

  public final HexadecimalNumber add (HexadecimalNumber number) {
    return new HexadecimalNumber(value + number.getValue());
  }

  public final HexadecimalNumber sub (HexadecimalNumber number) {
    return new HexadecimalNumber(value - number.getValue());
  }

  public final HexadecimalNumber mul (HexadecimalNumber number) {
    return new HexadecimalNumber(value * number.getValue());
  }

  public final HexadecimalNumber div (HexadecimalNumber number) {
    return new HexadecimalNumber(value / number.getValue());
  }

  @Override
  public final boolean isValid () {
    return true;
  }

  @Override
  public final String format () {
    return new HexadecimalFormatter().format(value);
  }
}
