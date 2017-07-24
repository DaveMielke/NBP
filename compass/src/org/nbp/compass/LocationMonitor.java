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

  private enum MonitorState {
    STOPPED,
    FAILED,
    STARTED;
  }

  protected abstract boolean startProvider ();
  protected abstract void stopProvider ();
  private MonitorState state = MonitorState.STOPPED;

  public final boolean isStarted () {
    return state != MonitorState.STOPPED;
  }

  public final void startMonitor () {
    if (state != MonitorState.STARTED) {
      if (startProvider()) {
        state = MonitorState.STARTED;
      } else {
        state = MonitorState.FAILED;
      }
    }
  }

  public final void stopMonitor () {
    switch (state) {
      case STARTED:
        stopProvider();
      case FAILED:
        state = MonitorState.STOPPED;
      case STOPPED:
        break;
    }
  }
}
