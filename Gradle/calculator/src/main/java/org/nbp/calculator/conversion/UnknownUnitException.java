package org.nbp.calculator.conversion;

public class UnknownUnitException extends ConversionException {
  public UnknownUnitException (String name) {
    super(("unknown unit: " + name));
  }
}
