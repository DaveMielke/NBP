package org.nbp.navigator.controls;
import org.nbp.navigator.*;

public class LocationMonitorControl extends ActivationLevelControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LocationMonitor;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_location;
  }

  @Override
  protected String getPreferenceKey () {
    return "location-monitor";
  }

  @Override
  protected ActivationLevel getEnumerationDefault () {
    return ApplicationDefaults.LOCATION_MONITOR;
  }

  @Override
  public ActivationLevel getEnumerationValue () {
    return ApplicationSettings.LOCATION_MONITOR;
  }

  @Override
  protected boolean setEnumerationValue (ActivationLevel value) {
    ApplicationSettings.LOCATION_MONITOR = value;
    onChange();
    return true;
  }

  @Override
  protected final void startTask () {
    LocationMonitor.startCurrentMonitor();
  }

  @Override
  protected final void stopTask () {
    LocationMonitor.stopCurrentMonitor();
  }

  public LocationMonitorControl () {
    super();
  }
}
