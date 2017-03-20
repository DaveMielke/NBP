package org.nbp.calculator.conversion;

import java.util.Stack;

public class Conversion {
  private Conversion () {
  }

  private final static Object LOCK = new Object();
  private static Conversion instance = null;

  public final static Conversion getInstance () {
    synchronized (LOCK) {
      if (instance == null) instance = new Conversion();
    }

    return instance;
  }

  public final UnitType[] getUnitTypes () {
    return UnitType.getUnitTypes();
  }

  public final UnitType getUnitType (String name) {
    return UnitType.getUnitType(name);
  }

  public final Unit[] getUnits () {
    return Unit.getUnits();
  }

  public final Unit[] getUnits (UnitType type) {
    return type.getUnits();
  }

  public final Unit getUnit (String name) {
    return Unit.getUnit(name);
  }

  public final static void verifyCompatibility (Unit unit1, Unit unit2) {
    if (unit1.getType() != unit2.getType()) {
      throw new IncompatibleUnitsException(unit1, unit2);
    }
  }

  public final static double convert (double value, Unit from, Unit to) {
    verifyCompatibility(from, to);
    Stack<Unit> stack = new Stack<Unit>();

    while (to != null) {
      stack.push(to);
      to = to.getReference();
    }

    while (!stack.contains(from)) {
      value -= from.getAdjustment();
      value *= from.getMultiplier();
      from = from.getReference();
    }

    while (stack.pop() != from);

    while (!stack.empty()) {
      to = stack.pop();
      value /= to.getMultiplier();
      value += to.getAdjustment();
    }

    return value;
  }

  public final double convert (double value, String from, String to) {
    return convert(value, getUnit(from), getUnit(to));
  }

  private final static boolean isInteger (double value) {
    if (Math.abs(value) >= 1E6) return false;
    return Math.rint(value) == value;
  }

  private final static void append (StringBuilder sb, double value) {
    if (isInteger(value)) {
      sb.append((long)value);
    } else {
      sb.append(value);
    }
  }

  private final static void append (StringBuilder sb, String string) {
    sb.append(string.replace('_', ' '));
  }

  public final static String makeDescription (Unit unit) {
    StringBuilder sb = new StringBuilder(unit.getSymbol());

    sb.append(" [");
    append(sb, unit.getName());
    sb.append("]");

    {
      final Unit reference = unit.getReference();

      if (reference != null) {
        sb.append(" (");

        {
          double value = unit.getMultiplier();

          if (value != 1.0) {
            if (value < 1.0) {
              double reciprocal = 1.0 / value;

              if (isInteger(reciprocal)) {
                sb.append("1/");
                value = reciprocal;
              } else {
                double quotient = Math.PI / value;

                if (isInteger(quotient)) {
                  sb.append("pi÷");
                  value = quotient;
                }
              }
            } else if (!isInteger(value)) {
              double quotient = value / Math.PI;

              if (isInteger(quotient)) {
                sb.append("pi×");
                value = quotient;
              }
            }

            append(sb, value);
            sb.append(' ');
          }
        }

        append(sb, reference.getName());

        {
          double adjustment = unit.getAdjustment();

          if (adjustment != 0.0) {
            char sign;
            if (adjustment > 0.0) {
              sign = '+';
            } else {
              sign = '-';
              adjustment = -adjustment;
            }

            sb.append(' ');
            sb.append(sign);
            sb.append(' ');
            append(sb, adjustment);
          }
        }

        sb.append(")");
      }
    }

    return sb.toString();
  }

  public final UnitType LENGTH = new LengthUnits();
  public final UnitType AREA = new AreaUnits();
  public final UnitType VOLUME = new VolumeUnits();

  public final UnitType ANGLE = new AngleUnits();
  public final UnitType TIME = new TimeUnits();
  public final UnitType TEMPERATURE = new TemperatureUnits();
}
