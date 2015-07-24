package org.nbp.b2g.ui;

public abstract class ShortFieldMap extends NumericFieldMap {
  public final Short getValue (String name) {
    Long value = getValue(name, Short.MIN_VALUE, Short.MAX_VALUE);
    if (value == null) return null;
    return value.shortValue();
  }

  public static void makeMaps (Class container, NumericFieldMap... maps) {
    makeMaps(container, Short.TYPE, maps);
  }

  public ShortFieldMap () {
    super();
  }
}
