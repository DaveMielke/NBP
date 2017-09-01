package org.nbp.navigator;

public abstract class OrientationMonitor extends NavigationMonitor {
  protected OrientationMonitor () {
    super();
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    getActivity().setOrientation(heading, pitch, roll);
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
