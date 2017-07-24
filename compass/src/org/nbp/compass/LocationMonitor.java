package org.nbp.compass;

import android.location.Location;

public abstract class LocationMonitor {
  protected LocationMonitor () {
  }

  protected final CompassActivity getCompassActivity () {
    return CompassActivity.getCompassActivity();
  }

  protected final void setLocation (Location location) {
    if (location != null) {
      getCompassActivity().setLocation(location);
    }
  }

  private enum ProviderState {
    STOPPED,
    FAILED,
    STARTED;
  }

  protected abstract boolean startProvider ();
  protected abstract void stopProvider ();
  private ProviderState providerState = ProviderState.STOPPED;

  public final boolean isStarted () {
    return providerState != ProviderState.STOPPED;
  }

  public final void startMonitor () {
    if (providerState != ProviderState.STARTED) {
      if (startProvider()) {
        providerState = ProviderState.STARTED;
      } else {
        providerState = ProviderState.FAILED;
      }
    }
  }

  public final void stopMonitor () {
    switch (providerState) {
      case STARTED:
        stopProvider();
      case FAILED:
        providerState = ProviderState.STOPPED;
      case STOPPED:
        break;
    }
  }
}
