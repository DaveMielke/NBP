package org.nbp.calculator.conversion;

public class DuplicateUnitException extends ConversionException {
  public DuplicateUnitException (String name) {
    super(("duplicate unit: " + name));
  }

  public DuplicateUnitException (Unit unit) {
    this(unit.getNameSingular());
  }
}
