package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class NumericFieldMap {
  private final static String LOG_TAG = NumericFieldMap.class.getName();

  protected abstract String getMapType ();
  protected abstract String getNamePrefix ();

  private final Map<String, Long> values = new HashMap<String, Long>();
  private final Map<Long, String> names = new HashMap<Long, String>();

  private void add (String name, long value) {
    values.put(name, value);
    names.put(value, name);
  }

  public final String getName (long value) {
    return names.get(value);
  }

  private Long getValue (String name, long minimum, long maximum) {
    Long value = values.get(name);
    if (value == null) return null;
    if (value < minimum) return null;
    if (value > maximum) return null;
    return value;
  }

  public final Long getLongValue (String name) {
    Long value = getValue(name, Long.MIN_VALUE, Long.MAX_VALUE);
    if (value == null) return null;
    return value.longValue();
  }

  public final Integer getIntegerValue (String name) {
    Long value = getValue(name, Integer.MIN_VALUE, Integer.MAX_VALUE);
    if (value == null) return null;
    return value.intValue();
  }

  public final Short getShortValue (String name) {
    Long value = getValue(name, Short.MIN_VALUE, Short.MAX_VALUE);
    if (value == null) return null;
    return value.shortValue();
  }

  public final Byte getByteValue (String name) {
    Long value = getValue(name, Byte.MIN_VALUE, Byte.MAX_VALUE);
    if (value == null) return null;
    return value.byteValue();
  }

  public final Character getCharacterValue (String name) {
    Long value = getValue(name, Character.MIN_VALUE, Character.MAX_VALUE);
    if (value == null) return null;
    return (char)value.longValue();
  }

  public static void makeMaps (Class container, Class type, NumericFieldMap... maps) {
    for (Field field : container.getFields()) {
      if (!field.getType().equals(type)) continue;
      Long value;

      int modifiers = field.getModifiers();
      if (!Modifier.isStatic(modifiers)) continue;
      if (!Modifier.isPublic(modifiers)) continue;
      if (!Modifier.isFinal(modifiers)) continue;

      try {
        value = field.getLong(null);
      } catch (IllegalAccessException exception) {
        continue;
      }

      String name = field.getName();

      for (NumericFieldMap map : maps) {
        String prefix = map.getNamePrefix();

        if (prefix != null) {
          if (!name.startsWith(prefix)) continue;
          name = name.substring(prefix.length());
        }

        {
          String oldName = map.getName(value);

          if (oldName == null) {
            map.add(name, value);
          } else {
            Log.w(LOG_TAG, String.format(
              "multiple names for %s #%d: %s & %s",
              map.getMapType(), value, oldName, name
            ));
          }
        }

        break;
      }
    }
  }

  public NumericFieldMap () {
    super();
  }
}
