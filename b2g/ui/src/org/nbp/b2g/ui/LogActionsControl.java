package org.nbp.b2g.ui;

public class LogActionsControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.logActions_control_label);
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
  protected boolean getBooleanValue () {
    return ApplicationParameters.CURRENT_LOG_ACTIONS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationParameters.CURRENT_LOG_ACTIONS = value;
    return true;
  }

  public LogActionsControl () {
    super(true);
  }
}
