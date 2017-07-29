package org.nbp.compass;
import org.nbp.compass.controls.*;

import org.nbp.common.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static LocationIntervalControl locationInterval = new LocationIntervalControl();
  public final static LocationRadiusControl locationRadius = new LocationRadiusControl();
  public final static ScreenOrientationControl screenOrientation = new ScreenOrientationControl();

  public final static DistanceUnitControl distanceUnit = new DistanceUnitControl();
  public final static SpeedUnitControl speedUnit = new SpeedUnitControl();
  public final static AngleUnitControl angleUnit = new AngleUnitControl();
  public final static RelativeDirectionControl relativeDirection = new RelativeDirectionControl();

  public final static SpeechVolumeControl speechVolume = new SpeechVolumeControl();
  public final static SpeechRateControl speechRate = new SpeechRateControl();
  public final static SpeechPitchControl speechPitch = new SpeechPitchControl();
  public final static SpeechBalanceControl speechBalance = new SpeechBalanceControl();

  public final static LogGeocodingControl logGeocoding = new LogGeocodingControl();
  public final static LogSensorsControl logSensors = new LogSensorsControl();
  public final static LocationProviderControl locationProvider = new LocationProviderControl();

  public final static Control[] ALL = new Control[] {
    locationInterval,
    locationRadius,
    screenOrientation,

    distanceUnit,
    speedUnit,
    angleUnit,
    relativeDirection,

    speechVolume,
    speechRate,
    speechPitch,
    speechBalance,

    logGeocoding,
    logSensors,
    locationProvider
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
