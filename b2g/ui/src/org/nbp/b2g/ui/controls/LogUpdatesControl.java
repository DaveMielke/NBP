package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogUpdatesControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.LogUpdates_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-updates";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_UPDATES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_UPDATES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_UPDATES = value;
    return true;
  }

  public LogUpdatesControl () {
    super(true);
  }
}
