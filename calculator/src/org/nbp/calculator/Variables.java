package org.nbp.calculator;

import java.util.Set;
import java.util.HashMap;

import org.nbp.common.CommonContext;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class Variables {
  public final static String RESULT = "$RESULT";

  private static class SystemVariables extends HashMap<String, SystemVariable> {
    public SystemVariables () {
      super();
    }
  }

  private final static SystemVariables systemVariables = new SystemVariables();

  private static void defineSystemVariable (String name, double value, String description) {
    systemVariables.put(name, new SystemVariable(value, description));
  }

  static {
    defineSystemVariable(
      "pi", Math.PI,
      "ratio of circumference to diameter"
    );

    defineSystemVariable(
      "sigma", 5.670367E-8,
      "Stefan-Boltzmann constant (W m^-2 K^-4)"
    );

    defineSystemVariable(
      "c", 299792458d,
      "speed of light in vacuum (m s^-1)"
    );

    defineSystemVariable(
      "e", Math.E,
      "base of natural logarithms"
    );

    defineSystemVariable(
      "h", 6.626070040E-34,
      "Planck constant (J s)"
    );

    defineSystemVariable(
      "k", 1.38064852E-23,
      "Boltzmann constant (J K^-1)"
    );

    defineSystemVariable(
      "F", 96485.33289,
      "Faraday constant (C mol^-1)"
    );

    defineSystemVariable(
      "G", 6.67408E-11,
      "Newtonian constant of gravitation (m^3 kg^-1 s^-2)"
    );

    defineSystemVariable(
      "L", 6.022140857E23,
      "Avogadro constant (mol^-1)"
    );

    defineSystemVariable(
      "R", 8.3144598,
      "molar gas constant (J mol^-1 K^-1)"
    );
  }

  public static String[] getSystemVariableNames () {
    Set<String> names = systemVariables.keySet();
    return names.toArray(new String[names.size()]);
  }

  public static SystemVariable getSystemVariable (String name) {
    return systemVariables.get(name);
  }

  private static Context getContext () {
    return CommonContext.getContext();
  }

  private static SharedPreferences getUserVariables () {
    return getContext().getSharedPreferences("variables", Context.MODE_PRIVATE);
  }

  public static Double get (String name) {
    {
      SharedPreferences variables = getUserVariables();

      if (variables.contains(name)) {
        return Double.valueOf(variables.getString(name, "0f"));
      }
    }

    {
      SystemVariable variable = getSystemVariable(name);
      if (variable != null) return variable.getValue();
    }

    return null;
  }

  public static boolean set (String name, double value) {
    if (systemVariables.containsKey(name)) return false;
    getUserVariables().edit().putString(name, Double.toString(value)).apply();
    return true;
  }

  private Variables () {
  }
}
