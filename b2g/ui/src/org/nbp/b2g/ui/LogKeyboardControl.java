package org.nbp.b2g.ui;

public class LogKeyboardControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.LogKeyboard_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-keyboard";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_KEYBOARD;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.LOG_KEYBOARD;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_KEYBOARD = value;
    return true;
  }

  public LogKeyboardControl () {
    super(true);
  }
}
