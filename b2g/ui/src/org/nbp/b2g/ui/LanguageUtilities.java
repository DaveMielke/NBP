package org.nbp.b2g.ui;

import java.lang.reflect.*;

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

  public static Object invokeInstanceMethod (
    Object instanceObject, String methodName,
    Class[] argumentTypes, Object... argumentObjects
  ) {
    Class instanceClass = instanceObject.getClass();
    String className = instanceClass.getName();
    String methodReference = className + "." + methodName;

    try {
      Method method = instanceClass.getMethod(methodName, argumentTypes);
      return method.invoke(instanceObject, argumentObjects);
    } catch (NoSuchMethodException exception) {
      Log.w(LOG_TAG, "cannot find method: " + methodReference);
    } catch (IllegalAccessException exception) {
      Log.w(LOG_TAG, "cannot access method: " + methodReference);
    } catch (InvocationTargetException exception) {
      Log.w(LOG_TAG, methodReference + " failed", exception.getCause());
    }

    return null;
  }

  public static Field getField (Class fieldClass, String fieldName) {
    try {
      return fieldClass.getField(fieldName);
    } catch (NoSuchFieldException exception) {
      String fieldReference = fieldClass.getName() + "." + fieldName;
      Log.w(LOG_TAG, "cannot find field: " + fieldReference);
    }

    return null;
  }

  public static Integer getInstanceIntField (Object instanceObject, String fieldName) {
    Class fieldClass = instanceObject.getClass();
    Field field = getField(fieldClass, fieldName);

    if (field != null) {
      try {
        return field.getInt(instanceObject);
      } catch (IllegalAccessException exception) {
        String fieldReference = fieldClass.getName() + "." + fieldName;
        Log.w(LOG_TAG, "cannot access field: " + fieldReference);
      }
    }

    return null;
  }

  private LanguageUtilities () {
  }
}
