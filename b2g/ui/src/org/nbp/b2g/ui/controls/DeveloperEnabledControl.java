package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class DeveloperEnabledControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return toHeader(R.string.DeveloperEnabled_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "developer-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.DEVELOPER_ENABLED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.DEVELOPER_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.DEVELOPER_ENABLED = value;
    return true;
  }

  public DeveloperEnabledControl () {
    super(ControlGroup.GENERAL);
  }
}
