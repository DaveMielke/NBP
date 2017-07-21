package org.nbp.compass;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static String toBearingString (float degrees) {
    return String.format("%d°", Math.round(degrees));
  }

  public final static String toCoordinateString (double degrees) {
    return String.format("%.5f°", degrees);
  }

  public final static String toCoordinatesString (double latitude, double longitude) {
    return String.format("[%.5f,%.5f]", latitude, longitude);
  }

  private final static String toCoordinateString (double degrees, char positive, char negative) {
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

  public final static String toLatitudeString (double degrees) {
    return toCoordinateString(degrees, 'N', 'S');
  }

  public final static String toLongitudeString (double degrees) {
    return toCoordinateString(degrees, 'E', 'W');
  }

  private final static String[] POINT_ACRONYMS = new String[] {
    "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
    "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"
  };

  private final static int POINT_COUNT = POINT_ACRONYMS.length;
  private final static float POINTS_PER_CIRCLE = (float)POINT_COUNT;
  private final static float DEGREES_PER_CIRCLE = 360f;
  private final static float DEGREES_PER_POINT = DEGREES_PER_CIRCLE / POINTS_PER_CIRCLE;

  public final static String toPointString (float degrees) {
    degrees += DEGREES_PER_CIRCLE;
    degrees %= DEGREES_PER_CIRCLE;
    int point = Math.round(degrees / DEGREES_PER_POINT);

    return String.format("%s%+d°",
      POINT_ACRONYMS[point % POINT_COUNT],
      Math.round(degrees - ((float)point * DEGREES_PER_POINT))
    );
  }

  public final static String toMagnitudeString (double magnitude, Unit unit) {
    return String.format("%d%s",
      Math.round(magnitude * unit.getConversion()), unit.getAcronym()
    );
  }

  public final static String toDistanceString (double distance) {
    return toMagnitudeString(distance, ApplicationSettings.DISTANCE_UNIT);
  }

  public final static String toSpeedString (float speed) {
    return toMagnitudeString(speed, ApplicationSettings.SPEED_UNIT);
  }
}
