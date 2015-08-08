package org.nbp.b2g.ui;

public class LogBrailleControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.LogBraille_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-braille";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_LOG_BRAILLE;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.LOG_BRAILLE;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_BRAILLE = value;
    return true;
  }

  public LogBrailleControl () {
    super(true);
  }
}
