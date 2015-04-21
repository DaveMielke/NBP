package org.nbp.b2g.ui;

public class SpeechOnControl extends BooleanControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.speechOn_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-on";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_ON;
  }

  @Override
  protected boolean getBooleanValue () {
    return ApplicationParameters.CURRENT_SPEECH_ON;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationParameters.CURRENT_SPEECH_ON = value;
    return true;
  }

  public SpeechOnControl () {
    super();
  }
}
