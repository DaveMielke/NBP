package org.nbp.calculator;
import org.nbp.calculator.conversion.*;

import android.util.Log;

public class ConversionFunction extends RealFunction {
  private final static String LOG_TAG = ConversionFunction.class.getName();

  public final static char SEPARATOR = '2';

  private final String functionName;
  private final String fromName;
  private final String toName;
  private final Unit fromUnit;
  private final Unit toUnit;
  private final String functionSummary;

  public ConversionFunction (String name) {
    super("convert");

    functionName = name;
    int index = name.lastIndexOf(SEPARATOR, (name.length() - 2));

    if (index > 0) {
      fromName = name.substring(0, index);
      toName = name.substring(index+1);

      try {
        Conversion conversion = Conversion.getInstance();
        fromUnit = conversion.getUnit(fromName);
        toUnit = conversion.getUnit(toName);

        if (fromUnit.getType() != toUnit.getType()) {
          throw new UnitException("incompatible units");
        }

        {
          StringBuilder sb = new StringBuilder();
          sb.append("convert real number from ");
          sb.append(fromUnit.getName());
          sb.append(" to ");
          sb.append(toUnit.getName());
          functionSummary = sb.toString();
        }

        return;
      } catch (UnitException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
    }

    throw new IllegalArgumentException(("not a unit conversion function: " + name));
  }

  @Override
  public final String getName () {
    return functionName;
  }

  @Override
  public final String getSummary () {
    return functionSummary;
  }

  public final double convert (double argument) {
    return Conversion.convert(argument, fromUnit, toUnit);
  }
}
