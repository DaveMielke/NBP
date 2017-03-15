package org.nbp.calculator.conversion;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Unit {
  private final String[] unitNames;
  private final UnitType unitType;
  private final Unit referenceUnit;
  private final double valueMultiplier;
  private final double valueAdjustment;

  private static class UnitMap extends HashMap<String, Unit> {
    public UnitMap () {
      super();
    }
  }

  private final static UnitMap units = new UnitMap();

  private Unit (UnitType type, Unit reference, double multiplier, double adjustment, String... names) {
    unitNames = Arrays.copyOf(names, names.length);
    unitType = type;
    referenceUnit = reference;
    valueMultiplier = multiplier;
    valueAdjustment = adjustment;

    {
      UnitMap map = new UnitMap();

      for (String name : unitNames) {
        if (name != null) {
          if (units.containsKey(name)) {
            throw new UnitException(("duplicate unit: " + name));
          }

          map.put(name, this);
        }
      }

      units.putAll(map);
    }
  }

  public Unit (UnitType type, String... names) {
    this(type, null, 0, 0, names);
  }

  public Unit (Unit reference, double multiplier, double adjustment, String... names) {
    this(reference.getType(), reference, multiplier, adjustment, names);
  }

  public Unit (Unit reference, double multiplier, String... names) {
    this(reference, multiplier, 0, names);
  }

  public final String[] getNames () {
    return Arrays.copyOf(unitNames, unitNames.length);
  }

  public final String getSymbol () {
    return unitNames[0];
  }

  public final String getName () {
    return unitNames[1];
  }

  public final UnitType getType () {
    return unitType;
  }

  public final Unit getReference () {
    return referenceUnit;
  }

  public final double getMultiplier () {
    return valueMultiplier;
  }

  public final double getAdjustment () {
    return valueAdjustment;
  }

  public final static Unit get (String name) {
    Unit unit = units.get(name);
    if (unit != null) return unit;
    throw new UnitException(("unknown unit: " + name));
  }
}
