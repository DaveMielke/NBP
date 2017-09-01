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

  private final boolean isStarted () {
    return state != MonitorState.STOPPED;
  }

  private final void start () {
    if (state != MonitorState.STARTED) {
      if (startMonitor()) {
        state = MonitorState.STARTED;
      } else {
        state = MonitorState.FAILED;
      }
    }
  }

  private final void stop () {
    switch (state) {
      case STARTED:
        stopMonitor();
      case FAILED:
        state = MonitorState.STOPPED;
      case STOPPED:
        break;
    }
  }

  public enum Reason {
    NAVIGATION_ACTIVITY, LOCATION_MONITOR;

    public final int getBit () {
      return 1 << ordinal();
    }
  }

  private final static OrientationMonitor orientationMonitor = new SensorOrientationMonitor();
  private static int currentReasons = 0;

  private final static void applyCurrentReasons () {
    if (currentReasons != 0) {
      orientationMonitor.start();
    } else {
      orientationMonitor.stop();
    }
  }

  public final static void start (Reason reason) {
    currentReasons |= reason.getBit();
    applyCurrentReasons();
  }

  public final static void stop (Reason reason) {
    currentReasons &= ~reason.getBit();
    applyCurrentReasons();
  }
}
