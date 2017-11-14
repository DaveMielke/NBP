package org.nbp.calculator;

import java.util.Set;
import java.util.LinkedHashMap;

import static org.nbp.common.CommonContext.getContext;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class Variables {
  private Variables () {
  }

  private static class PredefinedVariables extends LinkedHashMap<String, PredefinedVariable> {
    public PredefinedVariables () {
      super();
    }
  }

  private final static PredefinedVariables predefinedVariables = new PredefinedVariables();

  private static void defineConstant (String name, AbstractNumber value, String description) {
    predefinedVariables.put(name, new PredefinedConstant(name, value, description));
  }

  private static void defineConstant (String name, double real, double imag, String description) {
    defineConstant(name, new ComplexNumber(real, imag), description);
  }

  private static void defineConstant (String name, double real, String description) {
    defineConstant(name, new ComplexNumber(real), description);
  }

  static {
    defineConstant(
      "gamma", 0.57721566490153286060,
      "Euler-Mascheroni constant"
    );

    defineConstant(
      "pi", 3.14159265358979323846,
      "ratio of circumference to diameter"
    );

    defineConstant(
      "sigma", 5.670367E-8,
      "Stefan-Boltzmann constant [W m^-2 K^-4]"
    );

    defineConstant(
      "c", 299792458d,
      "speed of light in vacuum [m s^-1]"
    );

    defineConstant(
      "e", 2.71828182845904523536,
      "base of natural logarithms"
    );

    defineConstant(
      "h", 6.626070040E-34,
      "Planck constant [J s]"
    );

    defineConstant(
      "i", 0d, 1d,
      "imaginary unit"
    );

    defineConstant(
      "k", 1.38064852E-23,
      "Boltzmann constant [J K^-1]"
    );

    defineConstant(
      "F", 96485.33289,
      "Faraday constant [C mol^-1]"
    );

    defineConstant(
      "G", 6.67408E-11,
      "Newtonian constant of gravitation [m^3 kg^-1 s^-2]"
    );

    defineConstant(
      "L", 6.022140857E23,
      "Avogadro constant [mol^-1]"
    );

    defineConstant(
      "R", 8.3144598,
      "molar gas constant [J mol^-1 K^-1]"
    );
  }

  public static String[] getPredefinedVariableNames () {
    return ApplicationUtilities.toArray(predefinedVariables.keySet());
  }

  public static Variable getPredefinedVariable (String name) {
    return predefinedVariables.get(name);
  }

  public static boolean removePredefinedVariable (String name) {
    return predefinedVariables.remove(name) != null;
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
      Variable variable = getPredefinedVariable(name);
      if (variable != null) return variable;
    }

    return null;
  }

  public static boolean set (String name, AbstractNumber value) {
    if (predefinedVariables.containsKey(name)) return false;
    getUserVariables().edit().putString(name, value.toString()).apply();
    return true;
  }
}
