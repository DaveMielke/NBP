package org.nbp.compass;

public abstract class EarthMath {
  private EarthMath () {
  }

  public final static double RADIUS = 6371E3; // meters

  public final static double haversineDistance (
    double startLatitude, double startLongitude,
    double endLatitude, double endLongitude
  ) {
    double fromLatitude = Math.toRadians(startLatitude);
    double toLatitude = Math.toRadians(endLatitude);
    double deltaLatitude = Math.toRadians(endLatitude - startLatitude);
    double deltaLongitude = Math.toRadians(endLongitude - startLongitude);

    double a = Math.sin(deltaLatitude / 2d) * Math.sin(deltaLatitude / 2d)
             + Math.cos(fromLatitude) * Math.cos(toLatitude)
             * Math.sin(deltaLongitude / 2d) * Math.sin(deltaLongitude / 2d);

    double angularDistance = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1d - a));
    return angularDistance * RADIUS;
  }

  public final static double sphericalDistance (
    double startLatitude, double startLongitude,
    double endLatitude, double endLongitude
  ) {
    double fromLatitude = Math.toRadians(startLatitude);
    double toLatitude = Math.toRadians(endLatitude);
    double deltaLongitude = Math.toRadians(endLongitude - startLongitude);

    return Math.acos(Math.sin(fromLatitude) * Math.sin(toLatitude) + Math.cos(fromLatitude) * Math.cos(toLatitude) * Math.cos(deltaLongitude)) * RADIUS;
  }

  public final static double equirectangularDistance (
    double startLatitude, double startLongitude,
    double endLatitude, double endLongitude
  ) {
    double fromLatitude = Math.toRadians(startLatitude);
    double toLatitude = Math.toRadians(endLatitude);
    double fromLongitude = Math.toRadians(startLongitude);
    double toLongitude = Math.toRadians(endLongitude);

    double x = (toLongitude - fromLongitude) * Math.cos((fromLatitude + toLatitude) / 2d);
    double y = toLatitude - fromLatitude;
    return Math.sqrt((x * x) + (y * y)) * RADIUS;
  }

  public final static double initialBearing (
    double startLatitude, double startLongitude,
    double endLatitude, double endLongitude
  ) {
    double fromLatitude = Math.toRadians(startLatitude);
    double toLatitude = Math.toRadians(endLatitude);
    double fromLongitude = Math.toRadians(startLongitude);
    double toLongitude = Math.toRadians(endLongitude);

    double y = Math.sin(toLongitude - fromLongitude) * Math.cos(toLatitude);
    double x = Math.cos(fromLatitude) * Math.sin(toLatitude)
             - Math.sin(fromLatitude) * Math.cos(toLatitude) * Math.cos(toLongitude - fromLongitude);
    return Math.toDegrees(Math.atan2(y, x));
  }
}
