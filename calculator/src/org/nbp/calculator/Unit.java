package org.nbp.calculator;

import java.util.Map;
import java.util.HashMap;

public class Unit {
  private final String unitName;
  private final String[] unitAliases;
  private final UnitType unitType;
  private final Unit referenceUnit;
  private final double valueMultiplier;
  private final double valueAdjustment;

  private final static Map<String, Unit> units
             = new HashMap<String, Unit>();

  private Unit (UnitType type, Unit reference, double multiplier, double adjustment, String name, String... aliases) {
    unitName = name;
    unitAliases = aliases;
    unitType = type;
    referenceUnit = reference;
    valueMultiplier = multiplier;
    valueAdjustment = adjustment;

    if (units.containsKey(unitName)) {
      throw new UnitException(("duplicate unit: " + unitName));
    }

    for (String alias : unitAliases) {
      if (units.containsKey(alias)) {
        throw new UnitException(("duplicate unit: " + alias));
      }
    }

    units.put(unitName, this);
    for (String alias : unitAliases) units.put(alias, this);
  }

  public Unit (UnitType type, String name, String... aliases) {
    this(type, null, 0, 0, name, aliases);
  }

  public Unit (Unit reference, double multiplier, double adjustment, String name, String... aliases) {
    this(reference.getType(), reference, multiplier, adjustment, name, aliases);
  }

  public Unit (Unit reference, double multiplier, String name, String... aliases) {
    this(reference, multiplier, 0, name, aliases);
  }

  public final UnitType getType () {
    return unitType;
  }

  public final Unit getReference () {
    return referenceUnit;
  }

  public final String getName () {
    return unitName;
  }

  public final String[] getAliases () {
    return unitAliases;
  }

  public final double getMultiplier () {
    return valueMultiplier;
  }

  public final double getAdjustment () {
    return valueAdjustment;
  }
}
