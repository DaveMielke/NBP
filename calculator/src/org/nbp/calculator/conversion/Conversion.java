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

  public final UnitType getUnitType (String name) {
    return UnitType.get(name);
  }

  public final Unit getUnit (String name) {
    return Unit.get(name);
  }

  public final static double convert (double value, Unit from, Unit to) {
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
    return convert(value, Unit.get(from), Unit.get(to));
  }

  public final UnitType DISTANCE = new Distance();
  public final UnitType AREA = new Area();
  public final UnitType VOLUME = new Volume();
  public final UnitType TEMPERATURE = new Temperature();
}
