package org.nbp.calculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.util.Log;

public abstract class Functions {
  private final static String LOG_TAG = Functions.class.getName();

  private static class FunctionMap extends HashMap<String, Function> {
    public FunctionMap () {
      super();
    }
  }

  private final static FunctionMap systemFunctions = new FunctionMap();

  private static class MethodMap extends HashMap<String, Method> {
    public MethodMap () {
      super();
    }
  }

  private static MethodMap getMethods (Class<?> container, Class<?> type) {
    MethodMap map = new MethodMap();

    for (Method method : container.getDeclaredMethods()) {
      int modifiers = method.getModifiers();
      if ((modifiers & Modifier.PUBLIC) == 0) continue;
      if ((modifiers & Modifier.STATIC) == 0) continue;

      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 1) continue;
      if (parameterTypes[0] != type) continue;

      if (method.getReturnType() != type) continue;
      map.put(method.getName(), method);
    }

    return map;
  }

  private static void addFunction (
    String name, Class<? extends Function> type, Method method
  ) {
    Constructor constructor = LanguageUtilities.getConstructor(type, Method.class);

    if (constructor != null) {
      Function function = (Function)LanguageUtilities.newInstance(constructor, method);

      if (function != null) {
        systemFunctions.put(name, function);
        return;
      }
    }

    Log.w(LOG_TAG, ("method not added: " + name));
  }

  private static void addFunction (
    String functionName, Class<? extends Function> functionType,
    MethodMap methodMap, String methodName
  ) {
    Method method = methodMap.get(methodName);

    if (method != null) {
      addFunction(functionName, functionType, method);
    } else {
      Log.w(LOG_TAG, ("method not found: " + methodName));
    }
  }

  private static void addFunction (
    String functionName, Class<? extends Function> functionType,
    MethodMap methodMap
  ) {
    addFunction(functionName, functionType, methodMap, functionName);
  }

  private static void addRealFunction (
    String functionName, MethodMap methodMap, String methodName
  ) {
    addFunction(functionName, Function.class, methodMap, methodName);
  }

  private static void addRealFunction (String functionName, MethodMap methodMap) {
    addRealFunction(functionName, methodMap, functionName);
  }

  private static void addTrigonometricFunction (String functionName, MethodMap methodMap) {
    addFunction(functionName, TrigonometricFunction.class, methodMap);
  }

  private static void addInverseTrigonometricFunction (String functionName, MethodMap methodMap) {
    addFunction(functionName, InverseTrigonometricFunction.class, methodMap);
  }

  private static void addRealFunctions () {
    MethodMap methods = getMethods(Math.class, double.class);

    addRealFunction("abs", methods);
    addRealFunction("round", methods, "rint");
    addRealFunction("floor", methods);
    addRealFunction("ceil", methods);

    addRealFunction("sqrt", methods);
    addRealFunction("cbrt", methods);

    addRealFunction("exp", methods);
    addRealFunction("log", methods);
    addRealFunction("log10", methods);

    addRealFunction("rd2dg", methods, "toDegrees");
    addRealFunction("dg2rd", methods, "toRadians");

    addTrigonometricFunction("sin", methods);
    addTrigonometricFunction("cos", methods);
    addTrigonometricFunction("tan", methods);

    addInverseTrigonometricFunction("asin", methods);
    addInverseTrigonometricFunction("acos", methods);
    addInverseTrigonometricFunction("atan", methods);

    addTrigonometricFunction("sinh", methods);
    addTrigonometricFunction("cosh", methods);
    addTrigonometricFunction("tanh", methods);
  }

  static {
    Log.d(LOG_TAG, "begin function definitions");
    addRealFunctions();
    Log.d(LOG_TAG, "end function definitions");
  }

  public static Function get (String name) {
    return systemFunctions.get(name);
  }

  private Functions () {
  }
}
