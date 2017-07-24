package org.nbp.compass;

import org.nbp.common.LazyInstantiator;

public enum LocationProvider {
  BEST(BestLocationMonitor.class),
  GPS(GPSLocationMonitor.class),
  NETWORK(NetworkLocationMonitor.class),
  PASSIVE(PassiveLocationMonitor.class),
  FUSED(FusedLocationMonitor.class),
  ;

  private final LazyInstantiator<? extends LocationMonitor> monitorInstantiator;

  private LocationProvider (Class<? extends LocationMonitor> monitor) {
    monitorInstantiator = new LazyInstantiator(monitor);
  }

  public final LocationMonitor getMonitor () {
    return monitorInstantiator.get();
  }
}
