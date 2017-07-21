package org.nbp.compass;
import org.nbp.compass.controls.*;

import org.nbp.common.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static DistanceUnitControl distanceUnit = new DistanceUnitControl();
  public final static SpeedUnitControl speedUnit = new SpeedUnitControl();

  public final static LogAddressesControl logAddresses = new LogAddressesControl();
  public final static LogVectorsControl logVectors = new LogVectorsControl();

  public final static Control[] ALL = new Control[] {
    distanceUnit,
    speedUnit,

    logAddresses,
    logVectors
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
