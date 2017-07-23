package org.nbp.compass;
import org.nbp.compass.controls.*;

import org.nbp.common.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static DistanceUnitControl distanceUnit = new DistanceUnitControl();
  public final static SpeedUnitControl speedUnit = new SpeedUnitControl();
  public final static AngleUnitControl angleUnit = new AngleUnitControl();

  public final static LogGeocodingControl logGeocoding = new LogGeocodingControl();
  public final static LogSensorsControl logSensors = new LogSensorsControl();

  public final static Control[] ALL = new Control[] {
    distanceUnit,
    speedUnit,
    angleUnit,

    logGeocoding,
    logSensors
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
