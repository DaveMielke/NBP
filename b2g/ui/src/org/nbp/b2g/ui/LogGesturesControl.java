package org.nbp.b2g.ui;

public class LogGesturesControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.LogGestures_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-gestures";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_GESTURES;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_GESTURES;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_GESTURES = value;
    return true;
  }

  public LogGesturesControl () {
    super(true);
  }
}
