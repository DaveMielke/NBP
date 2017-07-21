package org.nbp.compass;

public abstract class ApplicationUtilities {
  private ApplicationUtilities () {
  }

  public final static String toString (float degrees) {
    return String.format("%d°", Math.round(degrees));
  }

  public final static String toString (double degrees) {
    return String.format("%.5f°", degrees);
  }

  public final static String toString (double latitude, double longitude) {
    return String.format("[%.5f,%.5f]", latitude, longitude);
  }

  public final static String toString (double degrees, char positive, char negative) {
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

  public final static String toString (double magnitude, Unit unit) {
    return String.format("%d%s",
      Math.round(magnitude * unit.getConversion()), unit.getAcronym()
    );
  }
}
