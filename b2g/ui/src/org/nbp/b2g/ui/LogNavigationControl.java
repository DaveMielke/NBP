package org.nbp.b2g.ui;

public class LogNavigationControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.logNavigation_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-navigation";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_NAVIGATION;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationParameters.CURRENT_LOG_NAVIGATION;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationParameters.CURRENT_LOG_NAVIGATION = value;
    return true;
  }

  public LogNavigationControl () {
    super(true);
  }
}
