package org.nbp.calculator.conversion;

public enum InternationalPrefix {
  DECA(1E1, "deca", "da"),
  HECTO(1E2, "hecto", "h"),
  KILO(1E3, "kilo", "k"),
  MEGA(1E6, "mega", "M"),
  GIGA(1E9, "giga", "G"),
  TERA(1E12, "tera", "T"),
  PETA(1E15, "peta", "P"),
  EXA(1E18, "exa", "E"),
  ZETTA(1E21, "zetta", "Z"),
  YOTTA(1E24, "yotta", "Y"),

  DECI(1E-1, "deci", "d"),
  CENTI(1E-2, "centi", "c"),
  MILLI(1E-3, "milli", "m"),
  MICRO(1E-6, "micro", "u"),
  NANO(1E-9, "nano", "n"),
  PICO(1E-12, "pico", "p"),
  FEMTO(1E-15, "femto", "f"),
  ATTO(1E-18, "atto", "a"),
  ZEPTO(1E-21, "zepto", "z"),
  YOCTO(1E-24, "yocto", "y"),

  KIBI(0X1P10, "kibi", "Ki"),
  MEBI(0X1P20, "mebi", "Mi"),
  GIBI(0X1P30, "gibi", "Gi"),
  TEBI(0X1P40, "tebi", "Ti"),
  PEBI(0X1P50, "pebi", "Pi"),
  EXBI(0X1P60, "exbi", "Ei"),
  ZEBI(0X1P70, "zebi", "Zi"),
  YOBI(0X1P80, "yobi", "Yi"),
  ;

  private final double prefixMultiplier;
  private final String prefixName;
  private final String prefixSymbol;

  public final double getMultiplier () {
    return prefixMultiplier;
  }

  public final String getName () {
    return prefixName;
  }

  public final String getSymbol () {
    return prefixSymbol;
  }

  private InternationalPrefix (double multiplier, String name, String symbol) {
    prefixMultiplier = multiplier;
    prefixName = name;
    prefixSymbol = symbol;
  }
}
