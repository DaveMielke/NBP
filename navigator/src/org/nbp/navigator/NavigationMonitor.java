package org.nbp.navigator;

import android.util.Log;

public abstract class NavigationMonitor extends NavigatorComponent {
  private final String LOG_TAG = getClass().getName();

  protected NavigationMonitor () {
    super();
  }

  protected final static NavigationFragment getFragment () {
    return NavigationFragment.getNavigationFragment();
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
      Log.d(LOG_TAG, "starting");

      if (startProvider()) {
        state = MonitorState.STARTED;
        Log.d(LOG_TAG, "started");
      } else {
        state = MonitorState.FAILED;
        Log.w(LOG_TAG, "failed");
      }
    }
  }

  public final void stop () {
    switch (state) {
      case STARTED:
        Log.d(LOG_TAG, "stopping");
        stopProvider();
      case FAILED:
        state = MonitorState.STOPPED;
        Log.d(LOG_TAG, "stopped");
      case STOPPED:
        break;
    }
  }
}
