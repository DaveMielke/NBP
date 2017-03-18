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
    return convert(value, getUnit(from), getUnit(to));
  }

  public final UnitType LENGTH = new Length();
  public final UnitType AREA = new Area();
  public final UnitType VOLUME = new Volume();
  public final UnitType TIME = new Time();
  public final UnitType TEMPERATURE = new Temperature();
}
