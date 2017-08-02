package org.nbp.compass;

import org.nbp.common.LazyInstantiator;

public enum LocationProvider {
  PASSIVE(PassiveLocationMonitor.class),
  NETWORK(NetworkLocationMonitor.class),
  GPS(GPSLocationMonitor.class),
  BEST(BestLocationMonitor.class),
  FUSED(FusedLocationMonitor.class),
  ; // end of enumeration

  private final LazyInstantiator<? extends LocationMonitor> monitorInstantiator;

  private LocationProvider (Class<? extends LocationMonitor> monitor) {
    monitorInstantiator = new LazyInstantiator(monitor);
  }

  public final LocationMonitor getMonitor () {
    return monitorInstantiator.get();
  }
}
