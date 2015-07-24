package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.HashMap;

import android.util.Log;

public abstract class FieldNameMap extends HashMap<Long, String> {
  private final static String LOG_TAG = FieldNameMap.class.getName();

  protected abstract String getMapType ();
  protected abstract String getNamePrefix ();

  public final String getName (long value) {
    return get(value);
  }

  public static void makeFieldNameMaps (Class container, Class<? extends Number> type, FieldNameMap... maps) {
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

      for (FieldNameMap map : maps) {
        String prefix = map.getNamePrefix();

        if (prefix != null) {
          if (!name.startsWith(prefix)) continue;
          name = name.substring(prefix.length());
        }

        {
          String oldName = map.get(value);

          if (oldName == null) {
            map.put(value, name);
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

  public FieldNameMap () {
    super();
  }
}
