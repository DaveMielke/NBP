package org.nbp.calculator.conversion;

public class IncompatibleUnitException extends ConversionException {
  public IncompatibleUnitException (Unit unit, UnitType type) {
    super(("incompatible unit: " + type.getName() + "." + unit.getNameSingular()));
  }
}
