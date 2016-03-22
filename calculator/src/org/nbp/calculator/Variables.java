package org.nbp.calculator;

import java.util.Set;
import java.util.LinkedHashMap;

import org.nbp.common.CommonContext;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class Variables {
  private static String[] toArray (Set<String> set) {
    return set.toArray(new String[set.size()]);
  }

  private static class SystemVariables extends LinkedHashMap<String, SystemVariable> {
    public SystemVariables () {
      super();
    }
  }

  private final static SystemVariables systemVariables = new SystemVariables();

  public static void setSystemVariable (String name, ComplexNumber value, String description) {
    systemVariables.put(name, new SystemVariable(value, description));
  }

  public static void setSystemVariable (String name, double value, String description) {
    setSystemVariable(name, new ComplexNumber(value), description);
  }

  static {
    setSystemVariable(
      "gamma", 0.57721566490153286060,
      "Euler-Mascheroni constant"
    );

    setSystemVariable(
      "pi", Math.PI,
      "ratio of circumference to diameter"
    );

    setSystemVariable(
      "sigma", 5.670367E-8,
      "Stefan-Boltzmann constant [W m^-2 K^-4]"
    );

    setSystemVariable(
      "c", 299792458d,
      "speed of light in vacuum [m s^-1]"
    );

    setSystemVariable(
      "e", Math.E,
      "base of natural logarithms"
    );

    setSystemVariable(
      "h", 6.626070040E-34,
      "Planck constant [J s]"
    );

    setSystemVariable(
      "i", new ComplexNumber(0, 1),
      "imaginary unit"
    );

    setSystemVariable(
      "k", 1.38064852E-23,
      "Boltzmann constant [J K^-1]"
    );

    setSystemVariable(
      "F", 96485.33289,
      "Faraday constant [C mol^-1]"
    );

    setSystemVariable(
      "G", 6.67408E-11,
      "Newtonian constant of gravitation [m^3 kg^-1 s^-2]"
    );

    setSystemVariable(
      "L", 6.022140857E23,
      "Avogadro constant [mol^-1]"
    );

    setSystemVariable(
      "R", 8.3144598,
      "molar gas constant [J mol^-1 K^-1]"
    );
  }

  public static String[] getSystemVariableNames () {
    return toArray(systemVariables.keySet());
  }

  public static SystemVariable getSystemVariable (String name) {
    return systemVariables.get(name);
  }

  public static boolean removeSystemVariable (String name) {
    return systemVariables.remove(name) != null;
  }

  private static Context getContext () {
    return CommonContext.getContext();
  }

  private static SharedPreferences getUserVariables () {
    return getContext().getSharedPreferences("variables", Context.MODE_PRIVATE);
  }

  public static String[] getUserVariableNames () {
    return toArray(getUserVariables().getAll().keySet());
  }

  public static boolean removeUserVariable (String name) {
    SharedPreferences variables = getUserVariables();
    if (!variables.contains(name)) return false;

    variables.edit().remove(name).apply();
    return true;
  }

  public static ComplexNumber get (String name) {
    {
      SharedPreferences variables = getUserVariables();

      if (variables.contains(name)) {
        String string = variables.getString(name, null);
        if (string != null) return ComplexNumber.valueOf(string);
      }
    }

    {
      SystemVariable variable = getSystemVariable(name);
      if (variable != null) return variable.getValue();
    }

    return null;
  }

  public static boolean set (String name, ComplexNumber value) {
    if (systemVariables.containsKey(name)) return false;
    getUserVariables().edit().putString(name, value.toString()).apply();
    return true;
  }

  public static boolean isNameCharacter (char character, boolean first) {
    if (Character.isLetter(character)) return true;
    if (first) return false;
    return Character.isDigit(character);
  }

  private Variables () {
  }
}
