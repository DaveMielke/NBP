package org.nbp.b2g.ui;

public class LogActionsControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.LogActions_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-actions";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_ACTIONS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_ACTIONS = value;
    return true;
  }

  public LogActionsControl () {
    super(true);
  }
}
