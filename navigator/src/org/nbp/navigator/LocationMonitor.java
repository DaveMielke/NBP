package org.nbp.navigator;

import android.location.Location;

public abstract class LocationMonitor extends NavigationMonitor {
  protected LocationMonitor () {
    super();
  }

  protected final void setLocation (Location location) {
    if (location != null) getActivity().setLocation(location);
  }

  private enum MonitorState {
    STOPPED, FAILED, STARTED;
  }

  protected abstract boolean startProvider ();
  protected abstract void stopProvider ();
  private MonitorState state = MonitorState.STOPPED;

  public final boolean isStarted () {
    return state != MonitorState.STOPPED;
  }

  public final void start () {
    if (state != MonitorState.STARTED) {
      if (startProvider()) {
        state = MonitorState.STARTED;
      } else {
        state = MonitorState.FAILED;
      }
    }
  }

  public final void stop () {
    switch (state) {
      case STARTED:
        stopProvider();
      case FAILED:
        state = MonitorState.STOPPED;
      case STOPPED:
        break;
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
