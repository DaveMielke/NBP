package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogNavigationControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.LogNavigation_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-navigation";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_NAVIGATION;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_NAVIGATION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_NAVIGATION = value;
    return true;
  }

  public LogNavigationControl () {
    super(ControlGroup.DEVELOPER);
  }
}
