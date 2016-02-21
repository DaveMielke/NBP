package org.nbp.common;

public abstract class ByteFieldMap extends NumericFieldMap {
  public final Byte getValue (String name) {
    Long value = getValue(name, Byte.MIN_VALUE, Byte.MAX_VALUE);
    if (value == null) return null;
    return value.byteValue();
  }

  public static void makeMaps (Class container, NumericFieldMap... maps) {
    makeMaps(container, Byte.TYPE, maps);
  }

  public ByteFieldMap () {
    super();
  }
}
