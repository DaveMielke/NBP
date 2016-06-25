package org.nbp.calculator;

import java.util.Map;
import java.util.HashMap;

public abstract class Multipliers {
  private final static Map<String, Double> multipliers =
               new HashMap<String, Double>();

  private final static void addMultiplier (Double multiplier, String... prefixes) {
    for (String prefix : prefixes) {
      multipliers.put(prefix, multiplier);
    }
  }

  static {
    addMultiplier(10E24, "yotta", "Y");
    addMultiplier(10E21, "zetta", "Z");
    addMultiplier(10E18, "exa", "E");
    addMultiplier(10E15, "peta", "P");
    addMultiplier(10E12, "tera", "T");
    addMultiplier(10E9, "giga", "G");
    addMultiplier(10E6, "mega", "M");
    addMultiplier(10E3, "kilo", "k");
    addMultiplier(10E2, "hecto", "h");
    addMultiplier(10E1, "deca", "da", "deka", "dk");
    addMultiplier(10E-1, "deci", "d");
    addMultiplier(10E-2, "centi", "c");
    addMultiplier(10E-3, "milli", "m");
    addMultiplier(10E-6, "micro", "u");
    addMultiplier(10E-9, "nano", "n");
    addMultiplier(10E-12, "pico", "p");
    addMultiplier(10E-15, "femto", "f");
    addMultiplier(10E-18, "atto", "a");
    addMultiplier(10E-21, "zepto", "z");
    addMultiplier(10E-24, "yocto", "y");

    addMultiplier(0X1P10, "kibi", "Ki");
    addMultiplier(0X1P20, "mebi", "Mi");
    addMultiplier(0X1P30, "gibi", "Gi");
    addMultiplier(0X1P40, "tebi", "Ti");
    addMultiplier(0X1P50, "pebi", "Pi");
    addMultiplier(0X1P60, "exbi", "Ei");
    addMultiplier(0X1P70, "zebi", "Zi");
    addMultiplier(0X1P80, "yobi", "Yi");
  }

  public final static Double getMultiplier (String prefix) {
    return multipliers.get(prefix);
  }

  private Multipliers () {
  }
}
