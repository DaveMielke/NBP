package org.nbp.compass;

import android.location.Location;

public abstract class NavigationMonitor {
  protected NavigationMonitor () {
  }

  protected final static NavigationActivity getActivity () {
    return NavigationActivity.getNavigationActivity();
  }

  protected final void runOnUiThread (Runnable task) {
    getActivity().runOnUiThread(task);
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    BaseActivity activity = getActivity();
    activity.setOrientationHeading(heading);
    activity.setOrientationPitch(pitch);
    activity.setOrientationRoll(roll);
  }

  protected final void setLocation (Location location) {
    if (location != null) getActivity().setLocation(location);
  }
}
