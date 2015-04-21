package org.nbp.b2g.ui;

public class LogKeysControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.logKeys_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-keys";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_KEYS;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationParameters.CURRENT_LOG_KEYS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationParameters.CURRENT_LOG_KEYS = value;
    return true;
  }

  public LogKeysControl () {
    super(true);
  }
}
