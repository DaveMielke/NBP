package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class DeveloperModeControl extends BooleanControl {
  @Override
  public int getLabel () {
    return R.string.control_label_DeveloperMode;
  }

  @Override
  protected String getPreferenceKey () {
    return "developer-mode";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.DEVELOPER_MODE;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.DEVELOPER_MODE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.DEVELOPER_MODE = value;
    return true;
  }

  public DeveloperModeControl () {
    super(ControlGroup.DEVELOPER);
  }
}
