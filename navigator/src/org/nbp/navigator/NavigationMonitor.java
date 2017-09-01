package org.nbp.navigator;

import android.location.Location;

public abstract class NavigationMonitor {
  protected NavigationMonitor () {
  }

  protected final static NavigationActivity getActivity () {
    return NavigationActivity.getNavigationActivity();
  }
}
