package org.nbp.calculator.conversion;

import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;

public class Unit {
  private final String[] unitNames;
  private final UnitType unitType;
  private final Unit referenceUnit;
  private final double valueMultiplier;
  private final double valueAdjustment;

  private static class UnitNameMap extends HashMap<String, Unit> {
    public UnitNameMap () {
      super();
    }
  }

  private final static Set<Unit> unitSet = new LinkedHashSet<Unit>();
  private final static UnitNameMap unitNameMap = new UnitNameMap();

  private Unit (UnitType type, Unit reference, double multiplier, double adjustment, String... names) {
    unitNames = Arrays.copyOf(names, names.length);
    unitType = type;
    referenceUnit = reference;
    valueMultiplier = multiplier;
    valueAdjustment = adjustment;

    {
      UnitNameMap nameMap = new UnitNameMap();

      for (String name : unitNames) {
        if (name != null) {
          if (unitNameMap.containsKey(name)) {
            throw new DuplicateUnitException(name);
          }

          nameMap.put(name, this);
        }
      }

      unitNameMap.putAll(nameMap);
    }

    unitSet.add(this);
    unitType.addUnit(this);
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

  private final String getName (int index) {
    int count = unitNames.length;
    if (index < count) return unitNames[index];
    return unitNames[count - 1];
  }

  public final String getSymbol () {
    return getName(0);
  }

  public final String getNamePlural () {
    return getName(1);
  }

  public final String getNameSingular () {
    return getName(2);
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

  public final static Unit getUnit (String name) {
    Unit unit = unitNameMap.get(name);
    if (unit != null) return unit;
    throw new UnknownUnitException(name);
  }

  public final static Unit[] getUnits () {
    return unitSet.toArray(new Unit[unitSet.size()]);
  }
}
