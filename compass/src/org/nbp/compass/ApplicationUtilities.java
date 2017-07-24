package org.nbp.compass;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static float toHeading (float degrees) {
    degrees += AngleUnit.DEGREES_PER_CIRCLE;
    degrees %= AngleUnit.DEGREES_PER_CIRCLE;
    return degrees;
  }

  private final static long convertMagnitude (double magnitude, Unit unit) {
    return Math.round(magnitude * unit.getMultiplier());
  }

  public final static CharSequence toMagnitudeText (double magnitude, Unit unit, Float ifZero) {
    long conversion = convertMagnitude(magnitude, unit);

    if (ifZero != null) {
      if (conversion == 0) {
        conversion = convertMagnitude(ifZero, unit);
      }
    }

    return String.format("%d%s", conversion, unit.getSymbol());
  }

  public final static CharSequence toMagnitudeText (double magnitude, Unit unit) {
    return toMagnitudeText(magnitude, unit, null);
  }

  public final static CharSequence toDistanceText (double meters) {
    return toMagnitudeText(meters, ApplicationSettings.DISTANCE_UNIT);
  }

  public final static CharSequence toSpeedText (float metersPerSecond) {
    return toMagnitudeText(metersPerSecond, ApplicationSettings.SPEED_UNIT);
  }

  public final static CharSequence toAngleText (float degrees) {
    return toMagnitudeText(degrees, ApplicationSettings.ANGLE_UNIT);
  }

  public final static CharSequence toHeadingText (float degrees) {
    return toMagnitudeText(degrees, ApplicationSettings.ANGLE_UNIT, AngleUnit.DEGREES_PER_CIRCLE);
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

  public final static CharSequence toPointText (float degrees) {
    CompassPoint point = CompassPoint.getPoint(degrees);

    SpannableStringBuilder sb = new SpannableStringBuilder();
    sb.append(point.name());

    {
      Object span = point.getSpeechSpan();
      if (span != null) sb.setSpan(span, 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    {
      float offset = point.getOffset(degrees);
      if (offset >= 0f) sb.append('+');
      sb.append(toAngleText(offset));
    }

    return sb.subSequence(0, sb.length());
  }
}
