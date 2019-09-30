package org.nbp.navigator;

import org.nbp.common.speech.SpeechParameters;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static DistanceUnit DISTANCE_UNIT = DistanceUnit.FEET;
  public final static SpeedUnit SPEED_UNIT = SpeedUnit.MPH;
  public final static AngleUnit ANGLE_UNIT = AngleUnit.DEGREES;
  public final static RelativeDirection RELATIVE_DIRECTION = RelativeDirection.OCLOCK;

  public final static boolean ANNOUNCE_LOCATION = false;
  public final static ActivationLevel LOCATION_MONITOR = ActivationLevel.FOREGROUND;
  public final static int LOCATION_RADIUS = 6; // meters
  public final static int UPDATE_INTERVAL = 4 * Unit.MILLISECONDS_PER_SECOND; // milliseconds

  public final static ScreenOrientation SCREEN_ORIENTATION = ScreenOrientation.PORTRAIT;

  public final static String SPEECH_ENGINE = "";
  public final static float SPEECH_VOLUME = SpeechParameters.VOLUME_MAXIMUM;
  public final static float SPEECH_RATE = SpeechParameters.RATE_REFERENCE;
  public final static float SPEECH_PITCH = SpeechParameters.PITCH_REFERENCE;
  public final static float SPEECH_BALANCE = SpeechParameters.BALANCE_CENTER;

  public final static boolean LOG_GEOCODING = false;
  public final static boolean LOG_SENSORS = false;
  public final static LocationProvider LOCATION_PROVIDER = LocationProvider.BEST;
}
