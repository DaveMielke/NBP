package org.nbp.calculator.conversion;

public class MassUnits extends UnitType {
  public MassUnits () {
    super("mass", true, "g", "grams", "gram");
  }

  public final InternationalUnit GRAM = (InternationalUnit)getBaseUnit();

  public final Unit TONNE = new Unit(
    GRAM.MEGA, 1.0,
    "t", "tonnes", "tonne"
  );

  public final Unit DALTON = new Unit(
    GRAM.YOCTO, 1.660539040,
    "Da", "daltons", "dalton"
  );

  public final Unit OUNCE = new Unit(
    GRAM, 28.34952,
    "oz", "ounces", "ounce"
  );

  public final Unit DRAM = new Unit(
    OUNCE, 1.0 / 16.0,
    "dr", "drams", "dram"
  );

  public final Unit POUND = new Unit(
    OUNCE, 16.0,
    "lb", "pounds", "pound"
  );

  public final Unit STONE = new Unit(
    POUND, 14.0,
    "stone", "stone", "stone"
  );

  public final Unit QUARTER = new Unit(
    STONE, 2.0,
    "quarter", "quarters", "quarter"
  );

  public final Unit HUNDREDWEIGHT = new Unit(
    QUARTER, 4.0,
    "hw", "hundredweight", "hundredweight"
  );

  public final Unit LONG_TON = new Unit(
    HUNDREDWEIGHT, 20.0,
    "tl", "long_tons", "long_ton"
  );

  public final Unit SHORT_TON = new Unit(
    POUND, 2000.0,
    "ts", "short_tons", "short_ton"
  );

  public final Unit CARAT = new Unit(
    GRAM.MILLI, 200.0,
    "ct", "carats", "carat"
  );

  public final Unit GRAIN = new Unit(
    GRAM.MILLI, 64.79891,
    "gr", "grains", "grain"
  );

  public final Unit PENNY_WEIGHT = new Unit(
    GRAIN, 24.0,
    "dwt", "penny_weight", "penny_weight"
  );

  public final Unit TROY_OUNCE = new Unit(
    PENNY_WEIGHT, 20.0,
    "ozt", "troy_ounces", "troy_ounce"
  );

  public final Unit TROY_POUND = new Unit(
    TROY_OUNCE, 12.0,
    "lbt", "troy_pounds", "troy_pound"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return POUND;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return GRAM.KILO;
  }
}
