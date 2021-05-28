package org.nbp.navigator;

import android.location.Location;

public abstract class LocationMonitor extends NavigationMonitor {
  protected LocationMonitor () {
    super();
  }

  private final static Object LOCK = new Object();
  private static Location latestLocation = null;

  protected final void setLocation (Location location) {
    if (location != null) {
      synchronized (LOCK) {
        latestLocation = location;
        getFragment().setLocation(location);
      }
    }
  }

  public final static Location getLocation () {
    synchronized (LOCK) {
      return latestLocation;
    }
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
