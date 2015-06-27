package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class UnicodeUtilities {
  private final static String LOG_TAG = UnicodeUtilities.class.getName();

  private static abstract class NameMap extends HashMap<Byte, String> {
    protected abstract String getMapType ();
    protected abstract String getNamePrefix ();

    public String getName (int value) {
      if (value < 0) return null;
      if (value > Byte.MAX_VALUE) return null;
      return get((byte)value);
    }

    public NameMap () {
      super();
    }
  }

  private final static NameMap directionalityNames = new NameMap() {
    @Override
    protected final String getMapType () {
      return "directionality";
    }

    @Override
    protected final String getNamePrefix () {
      return "DIRECTIONALITY_";
    }
  };

  private final static NameMap categoryNames = new NameMap() {
    @Override
    protected final String getMapType () {
      return "category";
    }

    @Override
    protected final String getNamePrefix () {
      return null;
    }
  };

  private final static NameMap[] nameMaps = new NameMap[] {
    directionalityNames,
    categoryNames
  };

  static {
    for (Field field : Character.class.getFields()) {
      if (!field.getType().equals(byte.class)) continue;
      Byte value;

      int modifiers = field.getModifiers();
      if (!Modifier.isStatic(modifiers)) continue;
      if (!Modifier.isPublic(modifiers)) continue;
      if (!Modifier.isFinal(modifiers)) continue;

      try {
        value = field.getByte(null);
      } catch (IllegalAccessException exception) {
        continue;
      }

      String name = field.getName();

      for (NameMap nameMap : nameMaps) {
        String prefix = nameMap.getNamePrefix();

        if (prefix != null) {
          if (!name.startsWith(prefix)) {
            continue;
          }

          name = name.substring(prefix.length());
        }

        name = name.replace('_', ' ').toLowerCase();
        String oldName = nameMap.get(value);

        if (oldName == null) {
          nameMap.put(value, name);
        } else {
          Log.w(LOG_TAG, String.format(
            "multiple names for Unicode %s #%d: %s & %s",
            nameMap.getMapType(), value, oldName, name
          ));
        }

        break;
      }
    }
  }

  public static String getDirectionalityName (int value) {
    return directionalityNames.getName(value);
  }

  public static String getCategoryName (int value) {
    return categoryNames.getName(value);
  }

  private UnicodeUtilities () {
  }
}
