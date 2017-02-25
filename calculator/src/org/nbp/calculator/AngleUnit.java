package org.nbp.calculator;

public enum AngleUnit {
  DEGREES("DEG", "Degrees",
    new Converter() {
      @Override
      public double toRadians (double angle) {
        return Math.toRadians(angle);
      }

      @Override
      public double fromRadians (double angle) {
        return Math.toDegrees(angle);
      }
    }
  ),

  RADIANS("RAD", "Radians",
    new Converter() {
      @Override
      public double toRadians (double angle) {
        return angle;
      }

      @Override
      public double fromRadians (double angle) {
        return angle;
      }
    }
  );

  public interface Converter {
    public double toRadians (double angle);
    public double fromRadians (double angle);
  }

  private final String unitLabel;
  private final String unitDescription;
  private final Converter unitConverter;

  public final String getLabel () {
    return unitLabel;
  }

  public final String getDescription () {
    return unitDescription;
  }

  public final Converter getConverter () {
    return unitConverter;
  }

  private AngleUnit (String label, String description, Converter converter) {
    unitLabel = label;
    unitDescription = description;
    unitConverter = converter;
  }
}
