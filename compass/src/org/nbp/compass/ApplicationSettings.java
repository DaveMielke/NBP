package org.nbp.compass;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static int UPDATE_INTERVAL = ApplicationDefaults.UPDATE_INTERVAL; // milliseconds
  public volatile static int LOCATION_RADIUS = ApplicationDefaults.LOCATION_RADIUS; // meters
  public volatile static ScreenOrientation SCREEN_ORIENTATION = ApplicationDefaults.SCREEN_ORIENTATION;

  public volatile static boolean ANNOUNCE_LOCATION = ApplicationDefaults.ANNOUNCE_LOCATION;

  public volatile static DistanceUnit DISTANCE_UNIT = ApplicationDefaults.DISTANCE_UNIT;
  public volatile static SpeedUnit SPEED_UNIT = ApplicationDefaults.SPEED_UNIT;
  public volatile static AngleUnit ANGLE_UNIT = ApplicationDefaults.ANGLE_UNIT;
  public volatile static RelativeDirection RELATIVE_DIRECTION = ApplicationDefaults.RELATIVE_DIRECTION;

  public volatile static float SPEECH_VOLUME = ApplicationDefaults.SPEECH_VOLUME;
  public volatile static float SPEECH_RATE = ApplicationDefaults.SPEECH_RATE;
  public volatile static float SPEECH_PITCH = ApplicationDefaults.SPEECH_PITCH;
  public volatile static float SPEECH_BALANCE = ApplicationDefaults.SPEECH_BALANCE;

  public volatile static boolean LOG_GEOCODING = ApplicationDefaults.LOG_GEOCODING;
  public volatile static boolean LOG_SENSORS = ApplicationDefaults.LOG_SENSORS;
  public volatile static LocationProvider LOCATION_PROVIDER = ApplicationDefaults.LOCATION_PROVIDER;
}
