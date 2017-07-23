package org.nbp.compass;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static CharSequence toBearingText (float degrees) {
    return String.format("%d°", Math.round(degrees));
  }

  public final static CharSequence toCoordinateText (double degrees) {
    return String.format("%.5f°", degrees);
  }

  public final static CharSequence toCoordinatesText (double latitude, double longitude) {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    sb.append(toCoordinateText(latitude));
    sb.append(',');
    sb.append(toCoordinateText(longitude));
    sb.append(']');
    return sb.toString();
  }

  private final static CharSequence toCoordinateText (double degrees, char positive, char negative) {
    char hemisphere;

    if (degrees > 0f) {
      hemisphere = positive;
    } else if (degrees < 0f) {
      hemisphere = negative;
      degrees = -degrees;
    } else {
      hemisphere = ' ';
    }

    StringBuilder sb = new StringBuilder();
    long value = Math.round(degrees * 3600f);

    sb.append(value % 60);
    sb.append('"');
    value /= 60;

    if (value > 0) {
      sb.insert(0, "' ");
      sb.insert(0, value%60);
      value /= 60;

      if (value > 0) {
        sb.insert(0, "° ");
        sb.insert(0, value);
      }
    }

    if (hemisphere != ' ') {
      sb.append(' ');
      sb.append(hemisphere);
    }

    return sb.toString();
  }

  public final static CharSequence toLatitudeText (double degrees) {
    return toCoordinateText(degrees, 'N', 'S');
  }

  public final static CharSequence toLongitudeText (double degrees) {
    return toCoordinateText(degrees, 'E', 'W');
  }

  private final static int POINT_COUNT = CompassPoint.values().length;
  private final static float POINTS_PER_CIRCLE = (float)POINT_COUNT;
  private final static float DEGREES_PER_CIRCLE = 360f;
  private final static float DEGREES_PER_POINT = DEGREES_PER_CIRCLE / POINTS_PER_CIRCLE;

  public final static CharSequence toPointText (float degrees) {
    degrees += DEGREES_PER_CIRCLE;
    degrees %= DEGREES_PER_CIRCLE;

    int ordinal = Math.round(degrees / DEGREES_PER_POINT);
    CompassPoint point = CompassPoint.values()[ordinal % POINT_COUNT];

    SpannableStringBuilder sb = new SpannableStringBuilder();
    sb.append(point.name());

    Object span = point.getSpeechSpan();
    if (span != null) sb.setSpan(span, 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    sb.append(String.format("%+d°", Math.round(degrees - ((float)ordinal * DEGREES_PER_POINT))));
    return sb.subSequence(0, sb.length());
  }

  public final static CharSequence toMagnitudeText (double magnitude, Unit unit) {
    return String.format("%d%s",
      Math.round(magnitude * unit.getMultiplier()), unit.getSymbol()
    );
  }

  public final static CharSequence toDistanceText (double distance) {
    return toMagnitudeText(distance, ApplicationSettings.DISTANCE_UNIT);
  }

  public final static CharSequence toSpeedText (float speed) {
    return toMagnitudeText(speed, ApplicationSettings.SPEED_UNIT);
  }
}
