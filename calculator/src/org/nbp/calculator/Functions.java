package org.nbp.calculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.Set;
import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.util.Log;

public abstract class Functions {
  private final static String LOG_TAG = Functions.class.getName();

  private static String[] toArray (Set<String> set) {
    return set.toArray(new String[set.size()]);
  }

  private static class FunctionMap extends HashMap<String, Function> {
    public FunctionMap () {
      super();
    }
  }

  private final static FunctionMap functionMap = new FunctionMap();

  private static Function addFunction (String name, Function function) {
    return functionMap.put(name, function);
  }

  private static Function addFunction (Class<? extends Function> type, Method method) {
    String name = method.getName();
    Constructor constructor = LanguageUtilities.getConstructor(type, Method.class);

    if (constructor != null) {
      Function function = (Function)LanguageUtilities.newInstance(constructor, method);
      if (function != null) return addFunction(name, function);
    }

    Log.w(LOG_TAG, ("method not added: " + name));
    return null;
  }

  private static void addFunctions (Operations operations) {
    Class<? extends Function> functionType = operations.getFunctionType();
    Class<?> argumentType = operations.getArgumentType();

    for (Method method : operations.getClass().getDeclaredMethods()) {
      int modifiers = method.getModifiers();
      if ((modifiers & Modifier.PUBLIC) == 0) continue;
      if ((modifiers & Modifier.STATIC) == 0) continue;

      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 1) continue;
      if (parameterTypes[0] != argumentType) continue;

      if (method.getReturnType() != argumentType) continue;
      addFunction(functionType, method);
    }
  }

  static {
    Log.d(LOG_TAG, "begin function definitions");
    addFunctions(new RealOperations());
    addFunctions(new TrigonometricOperations());
    addFunctions(new InverseTrigonometricOperations());
    addFunctions(new ComplexOperations());
    Log.d(LOG_TAG, "end function definitions");
  }

  public static String[] getNames () {
    return toArray(functionMap.keySet());
  }

  public static Function get (String name) {
    {
      Function function = functionMap.get(name);
      if (function != null) return function;
      if (functionMap.containsKey(name)) return null;
    }

    {
      try {
        Function function = new ConversionFunction(name);
        if (function != null) return addFunction(name, function);
      } catch (IllegalArgumentException exception) {
      }
    }

    return addFunction(name, null);
  }

  private Functions () {
  }
}
