package org.nbp.b2g.ui;

public class DeveloperActionsControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.developerActions_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "developer-actions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_DEVELOPER_ACTIONS;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.DEVELOPER_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.DEVELOPER_ACTIONS = value;
    return true;
  }

  public DeveloperActionsControl () {
    super(false);
  }
}
