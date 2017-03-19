package org.nbp.calculator;

public enum AngleUnit {
  DEGREE("DEG", R.string.description_angleUnit_degree,
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

  GRADIAN("GON", R.string.description_angleUnit_gradian,
    new Converter() {
      private final double GRADS_PER_DEGREE = 100d / 90d;

      @Override
      public double toRadians (double angle) {
        return Math.toRadians(angle / GRADS_PER_DEGREE);
      }

      @Override
      public double fromRadians (double angle) {
        return Math.toDegrees(angle) * GRADS_PER_DEGREE;
      }
    }
  ),

  RADIAN("RAD", R.string.description_angleUnit_radian,
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

  public final int getTitle () {
    return R.string.title_angleUnit;
  }

  private final String unitLabel;
  private final int unitDescription;
  private final Converter unitConverter;

  public final String getLabel () {
    return unitLabel;
  }

  public final int getDescription () {
    return unitDescription;
  }

  public final Converter getConverter () {
    return unitConverter;
  }

  private AngleUnit (String label, int description, Converter converter) {
    unitLabel = label;
    unitDescription = description;
    unitConverter = converter;
  }
}
