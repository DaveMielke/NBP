package org.nbp.compass;

import android.location.Location;

public abstract class LocationMonitor {
  protected final CompassActivity compassActivity;

  protected LocationMonitor (CompassActivity activity) {
    compassActivity = activity;
  }

  protected final void setLocation (Location location) {
    if (location != null) compassActivity.setLocation(location);
  }

  public abstract void start ();
  public abstract void stop ();
}
