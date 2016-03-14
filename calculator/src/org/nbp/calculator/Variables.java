package org.nbp.calculator;

import java.util.HashMap;

import org.nbp.common.CommonContext;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class Variables {
  public final static String RESULT = "$RESULT";

  private static class VariableMap extends HashMap<String, Double> {
    public VariableMap () {
    }
  }

  private final static VariableMap systemVariables = new VariableMap();

  static {
    systemVariables.put("pi", Math.PI);
    systemVariables.put("e", Math.E);
  }

  private static Context getContext () {
    return CommonContext.getContext();
  }

  private static SharedPreferences getUserVariables () {
    return getContext().getSharedPreferences("variables", Context.MODE_PRIVATE);
  }

  public static Double get (String name) {
    SharedPreferences variables = getUserVariables();

    return variables.contains(name)?
           Double.valueOf(variables.getString(name, "0f")):
           systemVariables.get(name);
  }

  public static boolean set (String name, double value) {
    if (systemVariables.containsKey(name)) return false;
    getUserVariables().edit().putString(name, Double.toString(value)).apply();
    return true;
  }

  private Variables () {
  }
}
