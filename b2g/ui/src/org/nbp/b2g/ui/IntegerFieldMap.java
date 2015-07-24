package org.nbp.b2g.ui;

public abstract class IntegerFieldMap extends NumericFieldMap {
  public final Integer getValue (String name) {
    Long value = getValue(name, Integer.MIN_VALUE, Integer.MAX_VALUE);
    if (value == null) return null;
    return value.intValue();
  }

  public static void makeMaps (Class container, NumericFieldMap... maps) {
    makeMaps(container, Integer.TYPE, maps);
  }

  public IntegerFieldMap () {
    super();
  }
}
