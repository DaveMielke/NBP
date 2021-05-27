package org.nbp.common;

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

  private final void add (String name, long value) {
    values.put(name, value);
    names.put(value, name);
  }

  public final String getName (long value) {
    return names.get(value);
  }

  protected final Long getValue (String name, long minimum, long maximum) {
    Long value = values.get(name);
    if (value == null) return null;
    if (value < minimum) return null;
    if (value > maximum) return null;
    return value;
  }

  protected static void makeMaps (Class container, Class type, NumericFieldMap... maps) {
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

  protected NumericFieldMap () {
    super();
  }
}
