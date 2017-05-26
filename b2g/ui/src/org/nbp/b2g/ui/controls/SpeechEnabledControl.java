package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SpeechEnabledControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechEnabled;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-enabled";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SPEECH_ENABLED;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SPEECH_ENABLED;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SPEECH_ENABLED = value;
    return true;
  }

  public SpeechEnabledControl () {
    super(ControlGroup.SPEECH);
  }
}
