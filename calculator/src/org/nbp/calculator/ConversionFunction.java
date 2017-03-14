package org.nbp.calculator;

import org.nbp.calculator.conversion.*;

public class ConversionFunction extends RealFunction {
  private final String fromName;
  private final String toName;
  private final Unit fromUnit;
  private final Unit toUnit;
  private final String functionSummary;

  public ConversionFunction (String name) {
    super(null);
    int index = name.indexOf('2');

    if ((index > 0) && (index < (name.length() - 1))) {
      fromName = name.substring(0, index);
      toName = name.substring(index+1);
      functionSummary = "convert " + fromName + " to " + toName;

      try {
        fromUnit = Unit.get(fromName);
        toUnit = Unit.get(toName);
        if (fromUnit.getType() == toUnit.getType()) return;
        throw new UnitException("incompatible units");
      } catch (UnitException exception) {
      }
    }

    throw new IllegalArgumentException(("not a unit conversion: " + name));
  }

  @Override
  public final String getSummary () {
    return functionSummary;
  }
}
