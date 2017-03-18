package org.nbp.calculator;

import java.util.Set;
import java.util.LinkedHashMap;

import static org.nbp.common.CommonContext.getContext;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class Variables {
  private Variables () {
  }

  private static class SystemVariables extends LinkedHashMap<String, SystemVariable> {
    public SystemVariables () {
      super();
    }
  }

  private final static SystemVariables systemVariables = new SystemVariables();

  private static void setSystemVariable (String name, AbstractNumber value, String description) {
    systemVariables.put(name, new SystemVariable(name, value, description));
  }

  private static void setSystemVariable (String name, double real, double imag, String description) {
    setSystemVariable(name, new ComplexNumber(real, imag), description);
  }

  private static void setSystemVariable (String name, double real, String description) {
    setSystemVariable(name, new ComplexNumber(real), description);
  }

  static {
    setSystemVariable(
      "gamma", 0.57721566490153286060,
      "Euler-Mascheroni constant"
    );

    setSystemVariable(
      "pi", 3.14159265358979323846,
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
      "e", 2.71828182845904523536,
      "base of natural logarithms"
    );

    setSystemVariable(
      "h", 6.626070040E-34,
      "Planck constant [J s]"
    );

    setSystemVariable(
      "i", 0d, 1d,
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
    return ApplicationUtilities.toArray(systemVariables.keySet());
  }

  public static Variable getSystemVariable (String name) {
    return systemVariables.get(name);
  }

  public static boolean removeSystemVariable (String name) {
    return systemVariables.remove(name) != null;
  }

  private static SharedPreferences getUserVariables () {
    return getContext().getSharedPreferences("variables", Context.MODE_PRIVATE);
  }

  public static String[] getUserVariableNames () {
    return ApplicationUtilities.toArray(getUserVariables().getAll().keySet());
  }

  public static boolean removeUserVariable (String name) {
    SharedPreferences variables = getUserVariables();
    if (!variables.contains(name)) return false;

    variables.edit().remove(name).apply();
    return true;
  }

  public static Variable get (String name) {
    {
      SharedPreferences variables = getUserVariables();

      if (variables.contains(name)) {
        final String value = variables.getString(name, null);

        if (value != null) {
          return new Variable(name) {
            @Override
            public AbstractNumber getValue () {
              try {
                return newNumber(value);
              } catch (NumberFormatException exception) {
                return null;
              }
            }
          };
        }
      }
    }

    {
      Variable variable = getSystemVariable(name);
      if (variable != null) return variable;
    }

    return null;
  }

  public static boolean set (String name, AbstractNumber value) {
    if (systemVariables.containsKey(name)) return false;
    getUserVariables().edit().putString(name, value.toString()).apply();
    return true;
  }
}
