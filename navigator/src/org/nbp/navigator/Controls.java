package org.nbp.navigator;
import org.nbp.navigator.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  // The unit control objects need to be constructed first because
  // formatting the values of some of the other controls relies on them.
  public final static DistanceUnitControl distanceUnit = new DistanceUnitControl();
  public final static SpeedUnitControl speedUnit = new SpeedUnitControl();
  public final static AngleUnitControl angleUnit = new AngleUnitControl();
  public final static RelativeDirectionControl relativeDirection = new RelativeDirectionControl();

  public final static AnnounceLocationControl announceLocation = new AnnounceLocationControl();
  public final static LocationMonitorControl locationMonitor = new LocationMonitorControl();
  public final static LocationRadiusControl locationRadius = new LocationRadiusControl();
  public final static UpdateIntervalControl updateInterval = new UpdateIntervalControl();

  public final static ScreenOrientationControl screenOrientation = new ScreenOrientationControl();

  public final static SpeechVolumeControl speechVolume = new SpeechVolumeControl();
  public final static SpeechRateControl speechRate = new SpeechRateControl();
  public final static SpeechPitchControl speechPitch = new SpeechPitchControl();
  public final static SpeechBalanceControl speechBalance = new SpeechBalanceControl();

  public final static LogGeocodingControl logGeocoding = new LogGeocodingControl();
  public final static LogSensorsControl logSensors = new LogSensorsControl();
  public final static LocationProviderControl locationProvider = new LocationProviderControl();

  public final static Control[] ALL = new Control[] {
    // The unit settings need to be restored first because
    // restoring the values of some of the other settings relies on them.
    distanceUnit,
    speedUnit,
    angleUnit,
    relativeDirection,

    announceLocation,
    locationMonitor,
    locationRadius,
    updateInterval,

    screenOrientation,

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
