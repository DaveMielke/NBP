package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogKeyboardControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogKeyboard;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-keyboard";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_KEYBOARD;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_KEYBOARD;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_KEYBOARD = value;
    return true;
  }

  public LogKeyboardControl () {
    super(ControlGroup.DEVELOPER);
  }
}
