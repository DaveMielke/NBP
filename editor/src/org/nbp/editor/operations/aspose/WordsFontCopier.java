package org.nbp.editor.operations.aspose;
import org.nbp.editor.*;

import com.aspose.words.*;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public abstract class WordsFontCopier {
  private final static String LOG_TAG = WordsFontCopier.class.getName();

  private WordsFontCopier () {
  }

  private static boolean isInvokable (Method method) {
    return (method.getModifiers() & Modifier.PUBLIC) != 0;
  }

  private static class Property {
    public final Class type;
    public final Method getter;
    public final Method setter;

    public Property (Class type, Method getter, Method setter) {
      this.type = type;
      this.getter = getter;
      this.setter = setter;
    }
  }

  private static class Subobject {
    public final static String PACKAGE_NAME = "com.aspose.words";

    public final Class type;
    public final Method getter;

    public Subobject (Class type, Method getter) {
      this.type = type;
      this.getter = getter;
    }
  }

  private static class Copier {
    public final Property properties[];
    public final Subobject subobjects[];

    public Copier (Object object) {
      Class objectClass = object.getClass();

      List<Property> propertyList = new ArrayList<Property>();
      List<Subobject> subobjectList = new ArrayList<Subobject>();

      for (Method getter : objectClass.getDeclaredMethods()) {
        if (!isInvokable(getter)) continue;

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
          if (!isInvokable(setter)) continue;
          propertyList.add(new Property(propertyClass, getter, setter));
        } catch (NoSuchMethodException exception) {
          if (propertyClass.getPackage().getName().equals(Subobject.PACKAGE_NAME)) {
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

  private static Copier getCopier (Object object) {
    synchronized (copiers) {
      Copier copier = copiers.get(object);
      if (copier != null) return copier;

      copier = new Copier(object);
      copiers.put(object, copier);
      return copier;
    }
  }

  private static void copyProperties (Object source, Object target) throws Exception {
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
      copyProperties(getter.invoke(source), getter.invoke(target));
    }
  }

  public static void copyFont (Font source, Font target) throws Exception {
    copyProperties(source, target);
  }
}
