package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class LogSpeechControl extends BooleanControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.LogSpeech_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "log-speech";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_SPEECH;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_SPEECH;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_SPEECH = value;
    return true;
  }

  public LogSpeechControl () {
    super(ControlGroup.DEVELOPER);
  }
}
