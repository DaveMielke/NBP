package org.nbp.navigator;

import android.location.Location;

public abstract class NavigationMonitor {
  protected NavigationMonitor () {
  }

  protected final static NavigationActivity getActivity () {
    return NavigationActivity.getNavigationActivity();
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
}
