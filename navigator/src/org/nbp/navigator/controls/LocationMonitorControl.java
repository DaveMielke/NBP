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

  private final static OrientationMonitor.Reason ORIENTATION_MONITOR_REASON
                     = OrientationMonitor.Reason.LOCATION_MONITOR;

  @Override
  protected final void startTask () {
    LocationMonitor.startCurrentMonitor();
    OrientationMonitor.start(ORIENTATION_MONITOR_REASON);
  }

  @Override
  protected final void stopTask () {
    OrientationMonitor.stop(ORIENTATION_MONITOR_REASON);
    LocationMonitor.stopCurrentMonitor();
  }

  public LocationMonitorControl () {
    super();
  }
}
