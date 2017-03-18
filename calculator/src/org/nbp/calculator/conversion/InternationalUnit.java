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

  public final Unit DECA = newUnit(InternationalPrefix.DECA, "deca", "da");
  public final Unit HECTO = newUnit(InternationalPrefix.HECTO, "hecto", "h");
  public final Unit KILO = newUnit(InternationalPrefix.KILO, "kilo", "k");
  public final Unit MEGA = newUnit(InternationalPrefix.MEGA, "mega", "M");
  public final Unit GIGA = newUnit(InternationalPrefix.GIGA, "giga", "G");
  public final Unit TERA = newUnit(InternationalPrefix.TERA, "tera", "T");
  public final Unit PETA = newUnit(InternationalPrefix.PETA, "peta", "P");
  public final Unit EXA = newUnit(InternationalPrefix.EXA, "exa", "E");
  public final Unit ZETTA = newUnit(InternationalPrefix.ZETTA, "zetta", "Z");
  public final Unit YOTTA = newUnit(InternationalPrefix.YOTTA, "yotta", "Y");

  public final Unit DECI = newUnit(InternationalPrefix.DECI, "deci", "d");
  public final Unit CENTI = newUnit(InternationalPrefix.CENTI, "centi", "c");
  public final Unit MILLI = newUnit(InternationalPrefix.MILLI, "milli", "m");
  public final Unit MICRO = newUnit(InternationalPrefix.MICRO, "micro", "u");
  public final Unit NANO = newUnit(InternationalPrefix.NANO, "nano", "n");
  public final Unit PICO = newUnit(InternationalPrefix.PICO, "pico", "p");
  public final Unit FEMTO = newUnit(InternationalPrefix.FEMTO, "femto", "f");
  public final Unit ATTO = newUnit(InternationalPrefix.ATTO, "atto", "a");
  public final Unit ZEPTO = newUnit(InternationalPrefix.ZEPTO, "zepto", "z");
  public final Unit YOCTO = newUnit(InternationalPrefix.YOCTO, "yocto", "y");

  public final Unit KIBI = newUnit(InternationalPrefix.KIBI, "kibi", "Ki");
  public final Unit MEBI = newUnit(InternationalPrefix.MEBI, "mebi", "Mi");
  public final Unit GIBI = newUnit(InternationalPrefix.GIBI, "gibi", "Gi");
  public final Unit TEBI = newUnit(InternationalPrefix.TEBI, "tebi", "Ti");
  public final Unit PEBI = newUnit(InternationalPrefix.PEBI, "pebi", "Pi");
  public final Unit EXBI = newUnit(InternationalPrefix.EXBI, "exbi", "Ei");
  public final Unit ZEBI = newUnit(InternationalPrefix.ZEBI, "zebi", "Zi");
  public final Unit YOBI = newUnit(InternationalPrefix.YOBI, "yobi", "Yi");
}
