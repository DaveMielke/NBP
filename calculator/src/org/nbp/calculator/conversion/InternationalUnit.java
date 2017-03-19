package org.nbp.calculator.conversion;

public class InternationalUnit extends Unit {
  private final double rescaleMultiplier (double value) {
    String symbol = getSymbol();
    char character = symbol.charAt(symbol.length() - 1);

    if (character <= '9') {
      double multiplier = value;

      while (character > '1') {
        value *= multiplier;
        character -= 1;
      }
    }

    return value;
  }

  private final Unit newUnit (Double multiplier, String name, String symbol) {
    String[] names = getNames();
    names[0] = symbol + names[0];
    for (int i=1; i<names.length; i+=1) names[i] = name + names[i];
    return new Unit(this, rescaleMultiplier(multiplier), names);
  }

  public InternationalUnit (UnitType type, String... names) {
    super(type, names);
  }

  public InternationalUnit (Unit reference, double multiplier, String... names) {
    super(reference, multiplier, names);
  }

  public final Unit DECA = newUnit(InternationalMultiplier.DECA, "deca", "da");
  public final Unit HECTO = newUnit(InternationalMultiplier.HECTO, "hecto", "h");
  public final Unit KILO = newUnit(InternationalMultiplier.KILO, "kilo", "k");
  public final Unit MEGA = newUnit(InternationalMultiplier.MEGA, "mega", "M");
  public final Unit GIGA = newUnit(InternationalMultiplier.GIGA, "giga", "G");
  public final Unit TERA = newUnit(InternationalMultiplier.TERA, "tera", "T");
  public final Unit PETA = newUnit(InternationalMultiplier.PETA, "peta", "P");
  public final Unit EXA = newUnit(InternationalMultiplier.EXA, "exa", "E");
  public final Unit ZETTA = newUnit(InternationalMultiplier.ZETTA, "zetta", "Z");
  public final Unit YOTTA = newUnit(InternationalMultiplier.YOTTA, "yotta", "Y");

  public final Unit DECI = newUnit(InternationalMultiplier.DECI, "deci", "d");
  public final Unit CENTI = newUnit(InternationalMultiplier.CENTI, "centi", "c");
  public final Unit MILLI = newUnit(InternationalMultiplier.MILLI, "milli", "m");
  public final Unit MICRO = newUnit(InternationalMultiplier.MICRO, "micro", "u");
  public final Unit NANO = newUnit(InternationalMultiplier.NANO, "nano", "n");
  public final Unit PICO = newUnit(InternationalMultiplier.PICO, "pico", "p");
  public final Unit FEMTO = newUnit(InternationalMultiplier.FEMTO, "femto", "f");
  public final Unit ATTO = newUnit(InternationalMultiplier.ATTO, "atto", "a");
  public final Unit ZEPTO = newUnit(InternationalMultiplier.ZEPTO, "zepto", "z");
  public final Unit YOCTO = newUnit(InternationalMultiplier.YOCTO, "yocto", "y");

  public final Unit KIBI = newUnit(InternationalMultiplier.KIBI, "kibi", "Ki");
  public final Unit MEBI = newUnit(InternationalMultiplier.MEBI, "mebi", "Mi");
  public final Unit GIBI = newUnit(InternationalMultiplier.GIBI, "gibi", "Gi");
  public final Unit TEBI = newUnit(InternationalMultiplier.TEBI, "tebi", "Ti");
  public final Unit PEBI = newUnit(InternationalMultiplier.PEBI, "pebi", "Pi");
  public final Unit EXBI = newUnit(InternationalMultiplier.EXBI, "exbi", "Ei");
  public final Unit ZEBI = newUnit(InternationalMultiplier.ZEBI, "zebi", "Zi");
  public final Unit YOBI = newUnit(InternationalMultiplier.YOBI, "yobi", "Yi");
}
