package org.nbp.calculator.conversion;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;

public class UnitType {
  private final String typeName;
  private final Unit baseUnit;

  private final static Set<UnitType> unitTypeSet = new LinkedHashSet<UnitType>();
  private final Set<Unit> unitSet = new LinkedHashSet<Unit>();
  private final static Map<String, UnitType> unitTypeNameMap
             = new HashMap<String, UnitType>();

  public UnitType (String name, boolean international, String... unit) {
    if (unitTypeNameMap.containsKey(name)) {
      throw new DuplicateUnitException(name);
    }

    if (international) {
      baseUnit = new InternationalUnit(this, unit);
    } else {
      baseUnit = new Unit(this, unit);
    }

    typeName = name;
    unitTypeNameMap.put(typeName, this);
    unitTypeSet.add(this);
  }

  public final String getName () {
    return typeName;
  }

  public final Unit getBaseUnit () {
    return baseUnit;
  }

  public final static UnitType getUnitType (String name) {
    UnitType type = unitTypeNameMap.get(name);
    if (type != null) return type;
    throw new UnknownUnitException(name);
  }

  public final static UnitType[] getUnitTypes () {
    return unitTypeSet.toArray(new UnitType[unitTypeSet.size()]);
  }

  public final Unit[] getUnits () {
    return unitSet.toArray(new Unit[unitSet.size()]);
  }

  final void addUnit (Unit unit) {
    if (unit.getType() != this) {
      throw new IncompatibleUnitException(unit, this);
    }

    if (!unitSet.add(unit)) {
      throw new DuplicateUnitException(unit);
    }
  }
}
