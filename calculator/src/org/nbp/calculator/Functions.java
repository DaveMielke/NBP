package org.nbp.calculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.util.Log;

public abstract class Functions {
  private final static String LOG_TAG = Functions.class.getName();

  private static class FunctionMap extends HashMap<String, ComplexFunction> {
    public FunctionMap () {
      super();
    }
  }

  private final static FunctionMap systemFunctions = new FunctionMap();

  private static void addFunction (
    String name, Class<? extends ComplexFunction> type, Method method
  ) {
    Constructor constructor = LanguageUtilities.getConstructor(type, Method.class);

    if (constructor != null) {
      ComplexFunction function = (ComplexFunction)LanguageUtilities.newInstance(constructor, method);

      if (function != null) {
        systemFunctions.put(name, function);
        return;
      }
    }

    Log.w(LOG_TAG, ("method not added: " + name));
  }

  private static void addFunctions (
    Class<? extends ComplexFunction> functionType,
    Class<?> containerType, Class<?> argumentType
  ) {
    for (Method method : containerType.getDeclaredMethods()) {
      int modifiers = method.getModifiers();
      if ((modifiers & Modifier.PUBLIC) == 0) continue;
      if ((modifiers & Modifier.STATIC) == 0) continue;

      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 1) continue;
      if (parameterTypes[0] != argumentType) continue;

      if (method.getReturnType() != argumentType) continue;
      addFunction(method.getName(), functionType, method);
    }
  }

  static {
    Log.d(LOG_TAG, "begin function definitions");

    addFunctions(
      RealFunction.class,
      RealOperations.class,
      double.class
    );

    addFunctions(
      TrigonometricFunction.class,
      TrigonometricOperations.class,
      double.class
    );

    addFunctions(
      InverseTrigonometricFunction.class,
      InverseTrigonometricOperations.class,
      double.class
    );

    addFunctions(
      ComplexFunction.class,
      ComplexOperations.class,
      ComplexNumber.class
    );

    Log.d(LOG_TAG, "end function definitions");
  }

  public static ComplexFunction get (String name) {
    return systemFunctions.get(name);
  }

  private Functions () {
  }
}
