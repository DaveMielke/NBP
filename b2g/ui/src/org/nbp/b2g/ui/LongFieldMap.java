package org.nbp.b2g.ui;

public abstract class LongFieldMap extends NumericFieldMap {
  public final Long getValue (String name) {
    Long value = getValue(name, Long.MIN_VALUE, Long.MAX_VALUE);
    if (value == null) return null;
    return value.longValue();
  }

  public static void makeMaps (Class container, NumericFieldMap... maps) {
    makeMaps(container, Long.TYPE, maps);
  }

  public LongFieldMap () {
    super();
  }
}
