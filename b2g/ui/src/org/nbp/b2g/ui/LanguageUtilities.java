package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class LanguageUtilities {
  private final static String LOG_TAG = LanguageUtilities.class.getName();

  public static boolean canAssign (Class to, Class from) {
    return to.isAssignableFrom(from);
  }

  public static boolean canAssign (Class to, String from) {
    try {
      return canAssign(to, Class.forName(from));
    } catch (ClassNotFoundException exception) {
    }

    return false;
  }

  public static boolean canAssign (Class to, CharSequence from) {
    return canAssign(to, from.toString());
  }

  public static String getClassName (String name) {
    int index = name.lastIndexOf('.');
    if (index == -1) return name;
    return name.substring(index+1);
  }

  public static String getClassName (CharSequence name) {
    return getClassName(name.toString());
  }

  public static String getClassName (Class type) {
    return getClassName(type.getName());
  }

  private static String makeReference (Class container, String name, Class[] arguments) {
    StringBuilder sb = new StringBuilder();

    sb.append(container.getName());
    sb.append('.');
    sb.append(name);

    if (arguments != null) {
      sb.append('(');
      String delimiter = "";

      for (Class argument : arguments) {
        sb.append(delimiter);
        delimiter = ",";
        sb.append(argument.getName());
      }

      sb.append(')');
    }

    return sb.toString();
  }

  private static String makeReference (Method method) {
    return makeReference(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
  }

  public static Method getMethod (Class container, String name, Class[] types) {
    try {
      return container.getMethod(name, types);
    } catch (NoSuchMethodException exception) {
      Log.w(LOG_TAG, "cannot find method: " + makeReference(container, name, types));
    }

    return null;
  }

  public static Object invokeMethod (Method method, Object instance, Object... arguments) {
    try {
      return method.invoke(instance, arguments);
    } catch (IllegalAccessException exception) {
      Log.w(LOG_TAG, "cannot access method: " + makeReference(method));
    } catch (InvocationTargetException exception) {
      Log.w(LOG_TAG, "method failed: " + makeReference(method), exception.getCause());
    }

    return null;
  }

  public static Object invokeInstanceMethod (
    Object instance, String name,
    Class[] types, Object... arguments
  ) {
    Method method = getMethod(instance.getClass(), name, types);
    if (method == null) return null;
    return invokeMethod(method, instance, arguments);
  }

  public static Object invokeStaticMethod (
    Class container, String name,
    Class[] types, Object... arguments
  ) {
    if (types == null) types = new Class[0];
    Method method = getMethod(container, name, types);
    if (method == null) return null;
    return invokeMethod(method, null, arguments);
  }

  public static Object invokeStaticMethod (Class container, String name) {
    return invokeStaticMethod(container, name, null);
  }

  private static String makeReference (Class container, String name) {
    return makeReference(container, name, null);
  }

  private static String makeReference (Field field) {
    return makeReference(field.getDeclaringClass(), field.getName());
  }

  public static Field getField (Class container, String name) {
    try {
      return container.getField(name);
    } catch (NoSuchFieldException exception) {
      Log.w(LOG_TAG, "cannot find field: " + makeReference(container, name));
    }

    return null;
  }

  public static Object getField (Field field, Object instance) {
    try {
      return field.get(instance);
    } catch (IllegalAccessException exception) {
      Log.w(LOG_TAG, "cannot access field: " + makeReference(field));
    }

    return null;
  }

  public static Object getField (Field field) {
    return getField(field, null);
  }

  public static Object getInstanceField (Object instance, String name) {
    Field field = getField(instance.getClass(), name);
    if (field == null) return null;
    return getField(field, instance);
  }

  public static Object getStaticField (Class container, String name) {
    Field field = getField(container, name);
    if (field == null) return null;
    return getField(field);
  }

  private final static Map<Byte, String> unicodeCategoryMap = new HashMap<Byte, String>();

  private static void makeUnicodeCategoryMap () {
    synchronized (unicodeCategoryMap) {
      if (unicodeCategoryMap.isEmpty()) {
        for (Field field : Character.class.getFields()) {
          if (!field.getType().equals(byte.class)) continue;
          Byte value;

          int modifiers = field.getModifiers();
          if (!Modifier.isStatic(modifiers)) continue;
          if (!Modifier.isPublic(modifiers)) continue;
          if (!Modifier.isFinal(modifiers)) continue;

          String name = field.getName();
          if (name.startsWith("DIRECTIONALITY_")) continue;
          name = name.replace('_', ' ').toLowerCase();

          try {
            value = field.getByte(null);
          } catch (IllegalAccessException exception) {
            continue;
          }

          String oldName = unicodeCategoryMap.get(value);
          if (oldName == null) {
            unicodeCategoryMap.put(value, name);
          } else {
            Log.w(LOG_TAG, String.format(
              "multiple names for Unicode category #%d: %s & %s",
              value, oldName, name
            ));
          }
        }
      }
    }
  }

  public static String getUnicodeCategoryName (int category) {
    if (category < 0) return null;
    if (category > Byte.MAX_VALUE) return null;

    makeUnicodeCategoryMap();
    return unicodeCategoryMap.get((byte)category);
  }

  private LanguageUtilities () {
  }
}
