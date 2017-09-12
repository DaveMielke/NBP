package org.nbp.navigator;

import java.util.Set;
import java.util.HashSet;

import android.util.Log;

public abstract class OrientationMonitor extends NavigationMonitor {
  private final static String LOG_TAG = OrientationMonitor.class.getName();

  protected OrientationMonitor () {
    super();
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    getFragment().setOrientation(heading, pitch, roll);
  }

  public enum Reason {
    NAVIGATION_FRAGMENT, LOCATION_MONITOR;

    public final void log (String action) {
      Log.d(LOG_TAG, String.format("%s for %s", action, name()));
    }
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
    reason.log("start");
    if (currentReasons.add(reason)) applyCurrentReasons();
  }

  public final static void stop (Reason reason) {
    reason.log("stop");
    if (currentReasons.remove(reason)) applyCurrentReasons();
  }
}
