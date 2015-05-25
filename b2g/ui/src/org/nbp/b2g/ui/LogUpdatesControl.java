package org.nbp.b2g.ui;

public class LogUpdatesControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.logUpdates_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-updates";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_UPDATES;
  }

  @Override
  protected boolean getBooleanValue () {
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
