package org.nbp.b2g.ui;

public class DeveloperModeControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.DeveloperMode_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "developer-mode";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_DEVELOPER_MODE;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.DEVELOPER_MODE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.DEVELOPER_MODE = value;
    return true;
  }

  public DeveloperModeControl () {
    super(false);
  }
}
