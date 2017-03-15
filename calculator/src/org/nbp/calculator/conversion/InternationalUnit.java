package org.nbp.calculator.conversion;

public class InternationalUnit extends Unit {
  private final Unit newUnit (Double multiplier, String name, String symbol) {
    String[] names = getNames();
    names[0] = symbol + names[0];
    for (int i=1; i<names.length; i+=1) names[i] = name + names[i];
    return new Unit(this, multiplier, names);
  }

  public InternationalUnit (UnitType type, String... names) {
    super(type, names);
  }

  public final Unit YOTTA = newUnit(1E24, "yotta", "Y");
  public final Unit ZETTA = newUnit(1E21, "zetta", "Z");
  public final Unit EXA = newUnit(1E18, "exa", "E");
  public final Unit PETA = newUnit(1E15, "peta", "P");
  public final Unit TERA = newUnit(1E12, "tera", "T");
  public final Unit GIGA = newUnit(1E9, "giga", "G");
  public final Unit MEGA = newUnit(1E6, "mega", "M");
  public final Unit KILO = newUnit(1E3, "kilo", "k");
  public final Unit HECTO = newUnit(1E2, "hecto", "h");
  public final Unit DECA = newUnit(1E1, "deca", "da");
  public final Unit DEKA = newUnit(1E1, "deka", "dk");
  public final Unit DECI = newUnit(1E-1, "deci", "d");
  public final Unit CENTI = newUnit(1E-2, "centi", "c");
  public final Unit MILLI = newUnit(1E-3, "milli", "m");
  public final Unit MICRO = newUnit(1E-6, "micro", "u");
  public final Unit NANO = newUnit(1E-9, "nano", "n");
  public final Unit PICO = newUnit(1E-12, "pico", "p");
  public final Unit FEMTO = newUnit(1E-15, "femto", "f");
  public final Unit ATTO = newUnit(1E-18, "atto", "a");
  public final Unit ZEPTO = newUnit(1E-21, "zepto", "z");
  public final Unit YOCTO = newUnit(1E-24, "yocto", "y");

  public final Unit KIBI = newUnit(0X1P10, "kibi", "Ki");
  public final Unit MEBI = newUnit(0X1P20, "mebi", "Mi");
  public final Unit GIBI = newUnit(0X1P30, "gibi", "Gi");
  public final Unit TEBI = newUnit(0X1P40, "tebi", "Ti");
  public final Unit PEBI = newUnit(0X1P50, "pebi", "Pi");
  public final Unit EXBI = newUnit(0X1P60, "exbi", "Ei");
  public final Unit ZEBI = newUnit(0X1P70, "zebi", "Zi");
  public final Unit YOBI = newUnit(0X1P80, "yobi", "Yi");
}
