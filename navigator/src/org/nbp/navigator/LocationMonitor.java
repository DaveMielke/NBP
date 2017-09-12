package org.nbp.navigator;

import android.location.Location;

public abstract class LocationMonitor extends NavigationMonitor {
  protected LocationMonitor () {
    super();
  }

  protected final void setLocation (Location location) {
    if (location != null) getFragment().setLocation(location);
  }

  public final static LocationProvider getCurrentProvider () {
    return ApplicationSettings.LOCATION_PROVIDER;
  }

  public final static LocationMonitor getCurrentMonitor () {
    return getCurrentProvider().getMonitor();
  }

  public final static void startCurrentMonitor () {
    getCurrentMonitor().start();
  }

  public final static void stopCurrentMonitor () {
    getCurrentMonitor().stop();
  }

  public final static void restartCurrentMonitor () {
    LocationMonitor monitor = getCurrentMonitor();

    if (monitor.isStarted()) {
      monitor.stop();
      monitor.start();
    }
  }
}
