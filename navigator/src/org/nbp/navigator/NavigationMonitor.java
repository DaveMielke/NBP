package org.nbp.navigator;

import android.location.Location;

public abstract class NavigationMonitor {
  protected NavigationMonitor () {
  }

  protected final static NavigationActivity getActivity () {
    return NavigationActivity.getNavigationActivity();
  }

  protected final void setOrientation (float heading, float pitch, float roll) {
    getActivity().setOrientation(heading, pitch, roll);
  }

  protected final void setLocation (Location location) {
    if (location != null) getActivity().setLocation(location);
  }
}
