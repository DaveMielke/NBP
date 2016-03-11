package org.nbp.calculator;

import java.util.HashMap;

public abstract class Variables {
  public final static String RESULT = "$RESULT";

  private static class VariableMap extends HashMap<String, Double> {
    public VariableMap () {
    }
  }

  private final static VariableMap systemVariables = new VariableMap();
  private final static VariableMap userVariables = new VariableMap();

  static {
    systemVariables.put("pi", Math.PI);
    systemVariables.put("e", Math.E);
  }

  public static Double get (String name) {
    Double value = userVariables.get(name);
    if (value == null) value = systemVariables.get(name);
    return value;
  }

  public static void set (String name, double value) {
    userVariables.put(name, value);
  }

  private Variables () {
  }
}
