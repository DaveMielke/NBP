package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.aspose.words.*;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public abstract class WordsUtilities {
  private final static String LOG_TAG = WordsUtilities.class.getName();

  private WordsUtilities () {
  }

  public final static String PACKAGE_NAME = "com.aspose.words";

  public static boolean isPublic (Method method) {
    return (method.getModifiers() & Modifier.PUBLIC) != 0;
  }

  public static class Property {
    public final Class type;
    public final Method getter;
    public final Method setter;

    public Property (Class type, Method getter, Method setter) {
      this.type = type;
      this.getter = getter;
      this.setter = setter;
    }
  }

  public static class Subobject {
    public final Class type;
    public final Method getter;

    public Subobject (Class type, Method getter) {
      this.type = type;
      this.getter = getter;
    }
  }

  public static class Copier {
    private final Property properties[];
    private final Subobject subobjects[];

    public Copier (Object object) {
      Class objectClass = object.getClass();
      List<Property> propertyList = new ArrayList<Property>();
      List<Subobject> subobjectList = new ArrayList<Subobject>();

      for (Method getter : objectClass.getDeclaredMethods()) {
        if (!isPublic(getter)) continue;

        String getterName = getter.getName();
        if (!getterName.startsWith("get")) continue;

        // Skip setting the internals of a style as these should not be changed.
        if (getterName.equals("getItem")) continue;
        if (getterName.equals("getStyle")) continue;
        if (getterName.equals("getStyleName")) continue;
        if (getterName.equals("getStyleIdentifier")) continue;

        Class propertyClass = getter.getReturnType();
        String setterName = getterName.replace("get", "set");

        try {
          Method setter = objectClass.getDeclaredMethod(setterName, propertyClass);
          if (!isPublic(setter)) continue;
          propertyList.add(new Property(propertyClass, getter, setter));
        } catch (NoSuchMethodException exception) {
          if (propertyClass.getPackage().getName().equals(PACKAGE_NAME)) {
            subobjectList.add(new Subobject(propertyClass, getter));
          }
        }
      }

      properties = propertyList.toArray(new Property[propertyList.size()]);
      subobjects = subobjectList.toArray(new Subobject[subobjectList.size()]);
    }
  }

  private final static Map<Object, Copier> copiers =
               new HashMap<Object, Copier>();

  public static Copier getCopier (Object object) {
    synchronized (copiers) {
      Copier copier = copiers.get(object);
      if (copier != null) return copier;

      copier = new Copier(object);
      copiers.put(object, copier);
      return copier;
    }
  }

  public static void copyFormatting (Object source, Object target) throws Exception {
    Class sourceClass = source.getClass();
    Class targetClass = target.getClass();

    if (!sourceClass.equals(targetClass)) {
      throw new IllegalArgumentException(
        String.format(
          "both objects must be of the same type: %s != %s",
          sourceClass.getName(), targetClass.getName()
        )
      );
    }

    Copier copier = getCopier(source);

    for (Property property : copier.properties) {
      Object value;

      try {
        value = property.getter.invoke(source);
      } catch (IllegalAccessException exception) {
        Log.w(LOG_TAG,
          String.format(
            "can't get property: %s: %s",
            sourceClass.getSimpleName(),
            property.getter.getName()
          )
        );

        continue;
      }

      try {
        property.setter.invoke(target, value);
      } catch (IllegalAccessException exception) {
        Log.w(LOG_TAG,
          String.format(
            "can't set property: %s: %s",
            targetClass.getSimpleName(),
            property.setter.getName()
          )
        );

        continue;
      }
    }

    for (Subobject subobject : copier.subobjects) {
      Method getter = subobject.getter;
      copyFormatting(getter.invoke(source), getter.invoke(target));
    }
  }
}
