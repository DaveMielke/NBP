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

  private final Unit newUnit (InternationalPrefix prefix) {
    String[] names = getNames();
    names[0] = prefix.getSymbol() + names[0];

    String name = prefix.getName();
    for (int i=1; i<names.length; i+=1) names[i] = name + names[i];

    return new SecondaryUnit(this, rescaleMultiplier(prefix.getMultiplier()), names);
  }

  public InternationalUnit (UnitType type, String... names) {
    super(type, names);
  }

  public InternationalUnit (Unit reference, double multiplier, String... names) {
    super(reference, multiplier, names);
  }

  public final Unit DECA = newUnit(InternationalPrefix.DECA);
  public final Unit HECTO = newUnit(InternationalPrefix.HECTO);
  public final Unit KILO = newUnit(InternationalPrefix.KILO);
  public final Unit MEGA = newUnit(InternationalPrefix.MEGA);
  public final Unit GIGA = newUnit(InternationalPrefix.GIGA);
  public final Unit TERA = newUnit(InternationalPrefix.TERA);
  public final Unit PETA = newUnit(InternationalPrefix.PETA);
  public final Unit EXA = newUnit(InternationalPrefix.EXA);
  public final Unit ZETTA = newUnit(InternationalPrefix.ZETTA);
  public final Unit YOTTA = newUnit(InternationalPrefix.YOTTA);

  public final Unit DECI = newUnit(InternationalPrefix.DECI);
  public final Unit CENTI = newUnit(InternationalPrefix.CENTI);
  public final Unit MILLI = newUnit(InternationalPrefix.MILLI);
  public final Unit MICRO = newUnit(InternationalPrefix.MICRO);
  public final Unit NANO = newUnit(InternationalPrefix.NANO);
  public final Unit PICO = newUnit(InternationalPrefix.PICO);
  public final Unit FEMTO = newUnit(InternationalPrefix.FEMTO);
  public final Unit ATTO = newUnit(InternationalPrefix.ATTO);
  public final Unit ZEPTO = newUnit(InternationalPrefix.ZEPTO);
  public final Unit YOCTO = newUnit(InternationalPrefix.YOCTO);

  public final Unit KIBI = newUnit(InternationalPrefix.KIBI);
  public final Unit MEBI = newUnit(InternationalPrefix.MEBI);
  public final Unit GIBI = newUnit(InternationalPrefix.GIBI);
  public final Unit TEBI = newUnit(InternationalPrefix.TEBI);
  public final Unit PEBI = newUnit(InternationalPrefix.PEBI);
  public final Unit EXBI = newUnit(InternationalPrefix.EXBI);
  public final Unit ZEBI = newUnit(InternationalPrefix.ZEBI);
  public final Unit YOBI = newUnit(InternationalPrefix.YOBI);
}
