package org.nbp.compass;

public enum RelativeHeading {
  OCLOCK(
    new Formatter() {
      @Override
      public CharSequence formatHeading (float actual, float reference) {
        float heading = ApplicationUtilities.toHeading(actual - reference);
        int hour = Math.round(heading / DEGREES_PER_HOUR);
        if (hour == 0) hour = HOURS_PER_CLOCK;
        return String.format("@ %d o'clock", hour);
      }
    }
  ),
  ;

  private interface Formatter {
    public CharSequence formatHeading (float actual, float reference);
  }

  private final static int HOURS_PER_CLOCK = 12;
  private final static float DEGREES_PER_HOUR = AngleUnit.DEGREES_PER_CIRCLE / (float)HOURS_PER_CLOCK;

  private final Formatter headingFormatter;

  private RelativeHeading (Formatter formatter) {
    headingFormatter = formatter;
  }

  public final CharSequence toText (float actual, float reference) {
    return headingFormatter.formatHeading(actual, reference);
  }
}
