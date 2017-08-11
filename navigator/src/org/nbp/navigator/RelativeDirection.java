package org.nbp.navigator;

import org.nbp.common.CommonContext;

public enum RelativeDirection {
  POSITIVE_ANGLE(
    new Formatter() {
      @Override
      public CharSequence formatDirection (float direction) {
        return ApplicationUtilities.toHeadingText(direction);
      }
    }
  ),

  SIGNED_ANGLE(
    new Formatter() {
      @Override
      public CharSequence formatDirection (float direction) {
        return ApplicationUtilities.toAngleText(toSignedDegrees(direction));
      }
    }
  ),

  LEFT_RIGHT(
    new Formatter() {
      @Override
      public CharSequence formatDirection (float direction) {
        StringBuilder sb = new StringBuilder();

        float degrees = toSignedDegrees(direction);
        sb.append(ApplicationUtilities.toAngleText(Math.abs(degrees)));

        if (degrees != 0f) {
          sb.append(' ');

          int word = (degrees < 0f)? R.string.direction_left: R.string.direction_right;
          sb.append(CommonContext.getString(word));
        }

        return sb.toString();
      }
    }
  ),

  OCLOCK(
    new Formatter() {
      @Override
      public CharSequence formatDirection (float direction) {
        int hour = Math.round(direction / DEGREES_PER_HOUR);
        if (hour == 0) hour = HOURS_PER_CLOCK;
        return String.format("@ %d o'clock", hour);
      }
    }
  ),

  ; // end of enumeration

  private interface Formatter {
    public CharSequence formatDirection (float direction);
  }

  private final static int HOURS_PER_CLOCK = 12;
  private final static float DEGREES_PER_HOUR = AngleUnit.DEGREES_PER_FULL_TURN / (float)HOURS_PER_CLOCK;

  private final static float toSignedDegrees (float direction) {
    float degrees = (float)Math.rint(direction);

    if (degrees > AngleUnit.DEGREES_PER_HALF_TURN) {
      degrees -= AngleUnit.DEGREES_PER_FULL_TURN;
    }

    return degrees;
  }

  private final Formatter directionFormatter;

  private RelativeDirection (Formatter formatter) {
    directionFormatter = formatter;
  }

  public final CharSequence toText (float direction, float reference) {
    return directionFormatter.formatDirection(ApplicationUtilities.toUnsignedAngle(direction - reference));
  }
}
