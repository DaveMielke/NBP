package org.nbp.calculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.HashMap;

import org.nbp.common.LanguageUtilities;

import android.util.Log;

public abstract class Functions {
  private final static String LOG_TAG = Functions.class.getName();

  private static class MethodMap extends HashMap<String, Method> {
    public MethodMap () {
      super();
    }
  }

  private static MethodMap getMethodMap (
    Class<?> containerType, Class<?> argumentType
  ) {
    MethodMap map = new MethodMap();

    for (Method method : containerType.getDeclaredMethods()) {
      int modifiers = method.getModifiers();
      if ((modifiers & Modifier.PUBLIC) == 0) continue;
      if ((modifiers & Modifier.STATIC) == 0) continue;

      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 1) continue;
      if (parameterTypes[0] != argumentType) continue;

      if (method.getReturnType() != argumentType) continue;
      map.put(method.getName(), method);
    }

    return map;
  }

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
    Class<?> containerType, Class<?> argumentType,
    Class<? extends ComplexFunction> functionType
  ) {
    MethodMap map = getMethodMap(containerType, argumentType);

    for (String name : map.keySet()) {
      addFunction(name, functionType, map.get(name));
    }
  }

  private static void addFunction (
    String functionName, Class<? extends ComplexFunction> functionType,
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
    String functionName, Class<? extends ComplexFunction> functionType,
    MethodMap methodMap
  ) {
    addFunction(functionName, functionType, methodMap, functionName);
  }

  private static void addRealFunction (
    String functionName, MethodMap methodMap, String methodName
  ) {
    addFunction(functionName, RealFunction.class, methodMap, methodName);
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
    MethodMap methodMap = getMethodMap(Math.class, double.class);

    addRealFunction("abs", methodMap);

    addRealFunction("floor", methodMap);
    addRealFunction("round", methodMap, "rint");
    addRealFunction("ceil", methodMap);

    addRealFunction("sqrt", methodMap);
    addRealFunction("cbrt", methodMap);

    addRealFunction("exp", methodMap);
    addRealFunction("log", methodMap);
    addRealFunction("log10", methodMap);

    addRealFunction("rd2dg", methodMap, "toDegrees");
    addRealFunction("dg2rd", methodMap, "toRadians");

    addTrigonometricFunction("sin", methodMap);
    addTrigonometricFunction("cos", methodMap);
    addTrigonometricFunction("tan", methodMap);

    addInverseTrigonometricFunction("asin", methodMap);
    addInverseTrigonometricFunction("acos", methodMap);
    addInverseTrigonometricFunction("atan", methodMap);

    addRealFunction("sinh", methodMap);
    addRealFunction("cosh", methodMap);
    addRealFunction("tanh", methodMap);
  }

  static {
    Log.d(LOG_TAG, "begin function definitions");
    addRealFunctions();

    addFunctions(
      ComplexOperations.class,
      ComplexNumber.class,
      ComplexFunction.class
    );

    Log.d(LOG_TAG, "end function definitions");
  }

  public static ComplexFunction get (String name) {
    return systemFunctions.get(name);
  }

  private Functions () {
  }
}
