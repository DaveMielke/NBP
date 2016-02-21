package org.nbp.common;

public abstract class CharacterFieldMap extends NumericFieldMap {
  public final Character getValue (String name) {
    Long value = getValue(name, Character.MIN_VALUE, Character.MAX_VALUE);
    if (value == null) return null;
    return (char)value.longValue();
  }

  public static void makeMaps (Class container, NumericFieldMap... maps) {
    makeMaps(container, Character.TYPE, maps);
  }

  public CharacterFieldMap () {
    super();
  }
}
