package org.nbp.navigator;

import java.util.Set;
import java.util.HashSet;

public abstract class OrientationMonitor extends NavigationMonitor {
  protected OrientationMonitor () {
    super();
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    getActivity().setOrientation(heading, pitch, roll);
  }

  public enum Reason {
    NAVIGATION_ACTIVITY, LOCATION_MONITOR;
  }

  private final static OrientationMonitor orientationMonitor = new SensorOrientationMonitor();
  private final static Set<Reason> currentReasons = new HashSet<Reason>();

  private final static void applyCurrentReasons () {
    if (currentReasons.isEmpty()) {
      orientationMonitor.stop();
    } else {
      orientationMonitor.start();
    }
  }

  public final static void start (Reason reason) {
    if (currentReasons.add(reason)) applyCurrentReasons();
  }

  public final static void stop (Reason reason) {
    if (currentReasons.remove(reason)) applyCurrentReasons();
  }
}
