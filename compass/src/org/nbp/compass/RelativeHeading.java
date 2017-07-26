package org.nbp.compass;

public enum RelativeHeading {
  HEADING(
    new Formatter() {
      @Override
      public CharSequence formatHeading (float heading) {
        return ApplicationUtilities.toHeadingText(heading);
      }
    }
  ),

  RELATIVE(
    new Formatter() {
      @Override
      public CharSequence formatHeading (float heading) {
        float degrees = heading;
        if (degrees > 180f) degrees -= AngleUnit.DEGREES_PER_CIRCLE;
        return ApplicationUtilities.toAngleText(degrees);
      }
    }
  ),

  OCLOCK(
    new Formatter() {
      @Override
      public CharSequence formatHeading (float heading) {
        int hour = Math.round(heading / DEGREES_PER_HOUR);
        if (hour == 0) hour = HOURS_PER_CLOCK;
        return String.format("@ %d o'clock", hour);
      }
    }
  ),
  ;

  private interface Formatter {
    public CharSequence formatHeading (float heading);
  }

  private final static int HOURS_PER_CLOCK = 12;
  private final static float DEGREES_PER_HOUR = AngleUnit.DEGREES_PER_CIRCLE / (float)HOURS_PER_CLOCK;

  private final Formatter headingFormatter;

  private RelativeHeading (Formatter formatter) {
    headingFormatter = formatter;
  }

  public final CharSequence toText (float actual, float reference) {
    return headingFormatter.formatHeading(ApplicationUtilities.toHeading(actual - reference));
  }
}
