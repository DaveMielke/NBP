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
    if (value == 0.0) {
      sb.append('0');
      return;
    }

    if (Double.isNaN(value)) {
      sb.append(Double.toString(value));
      return;
    }

    if (value < 0.0) {
      sb.append('-');
      value = -value;
    }

    if (Double.isInfinite(value)) {
      sb.append(Double.toString(value));
      return;
    }

    if ((value < 1.0E3) && isInteger(value)) {
      sb.append((long)value);
      return;
    }

    {
      int exponent = Math.getExponent(value);

      if (Math.scalb(1.0, exponent) == value) {
        sb.append("2^");
        sb.append(exponent);
        return;
      }
    }

    String number = String.format("%.6G", value);
    int decimal = number.indexOf('.');
    int end = number.indexOf('E');
    int exponent = 0;

    if (end < 0) {
      end = number.length();
    } else {
      int start = end + 1;
      int length = number.length();
      boolean negative = false;

      if (start < length) {
        switch (number.charAt(start)) {
          case '-':
            negative = true;
            /* fall through */
          case '+':
            start += 1;
            break;
        }

        while ((start < length) && (number.charAt(start) == '0')) {
          start += 1;
        }

        if (start < length) {
          exponent = Integer.valueOf(number.substring(start));
          if (negative) exponent = -exponent;
        }
      }

      number = number.substring(0, end);
    }

    if (decimal >= 0) {
      while (end > 0) {
        if (number.charAt(--end) != '0') {
          if (end != decimal) end += 1;
          number = number.substring(0, end);
          break;
        }
      }
    }

    boolean isOne = number.equals("1");
    boolean hasExponent = exponent != 0;

    if (!isOne || !hasExponent) {
      sb.append(number);
      if (hasExponent) sb.append('×');
    }

    if (hasExponent) {
      sb.append("10^");
      sb.append(exponent);
    }
  }

  private final static void append (StringBuilder sb, String string) {
    sb.append(string.replace('_', ' '));
  }

  public final static String makeDescription (Unit unit) {
    StringBuilder sb = new StringBuilder(unit.getSymbol());

    sb.append(" [");
    append(sb, unit.getNamePlural());
    sb.append("]");

    {
      final Unit reference = unit.getReference();

      if (reference != null) {
        sb.append(" (");

        double multiplier = unit.getMultiplier();
        boolean hasMultiplier = multiplier != 1.0;

        double adjustment = unit.getAdjustment();
        boolean hasAdjustment = adjustment != 0.0;

        if (hasMultiplier || !hasAdjustment) {
          if (multiplier < 1.0) {
            double reciprocal = 1.0 / multiplier;

            if (isInteger(reciprocal)) {
              sb.append("1÷");
              multiplier = reciprocal;
            } else {
              double quotient = Math.PI / multiplier;

              if (isInteger(quotient)) {
                sb.append("pi÷");
                multiplier = quotient;
              }
            }
          } else if (!isInteger(multiplier)) {
            double quotient = multiplier / Math.PI;

            if (isInteger(quotient)) {
              sb.append("pi×");
              multiplier = quotient;
            }
          }

          append(sb, multiplier);
          sb.append(' ');
        }

        append(sb,
          hasMultiplier?
          reference.getNamePlural():
          reference.getNameSingular()
        );

        if (hasAdjustment) {
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
