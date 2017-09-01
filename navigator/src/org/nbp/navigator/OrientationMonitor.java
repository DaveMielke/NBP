package org.nbp.navigator;

public abstract class OrientationMonitor extends NavigationMonitor {
  protected OrientationMonitor () {
    super();
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    getActivity().setOrientation(heading, pitch, roll);
  }

  private enum MonitorState {
    STOPPED, FAILED, STARTED;
  }

  protected abstract boolean startMonitor ();
  protected abstract boolean stopMonitor ();
  private MonitorState state = MonitorState.STOPPED;

  public final boolean isStarted () {
    return state != MonitorState.STOPPED;
  }

  public final void start () {
    if (state != MonitorState.STARTED) {
      if (startMonitor()) {
        state = MonitorState.STARTED;
      } else {
        state = MonitorState.FAILED;
      }
    }
  }

  public final void stop () {
    switch (state) {
      case STARTED:
        stopMonitor();
      case FAILED:
        state = MonitorState.STOPPED;
      case STOPPED:
        break;
    }
  }

  private final static OrientationMonitor orientationMonitor = new SensorOrientationMonitor();

  public final static OrientationMonitor getMonitor () {
    return orientationMonitor;
  }
}
