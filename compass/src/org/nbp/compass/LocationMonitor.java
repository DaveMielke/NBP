package org.nbp.compass;

import android.location.Location;

public abstract class LocationMonitor {
  protected LocationMonitor () {
  }

  protected final CompassActivity getCompassActivity () {
    return CompassActivity.getCompassActivity();
  }

  protected final void setLocation (Location location) {
    if (location != null) {
      getCompassActivity().setLocation(location);
    }
  }

  protected abstract boolean startProvider ();
  protected abstract void stopProvider ();
  private boolean isStarted = false;

  public final boolean isStarted () {
    return isStarted;
  }

  public final void startMonitor () {
    if (!isStarted) {
      if (startProvider()) {
        isStarted = true;
      }
    }
  }

  public final void stopMonitor () {
    if (isStarted) {
      stopProvider();
      isStarted = false;
    }
  }
}
