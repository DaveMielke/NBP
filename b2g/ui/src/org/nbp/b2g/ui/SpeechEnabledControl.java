package org.nbp.b2g.ui;

public class SpeechEnabledControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.speechEnabled_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_ENABLED;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationSettings.SPEECH_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SPEECH_ENABLED = value;
    return true;
  }

  public SpeechEnabledControl () {
    super(false);
  }
}
