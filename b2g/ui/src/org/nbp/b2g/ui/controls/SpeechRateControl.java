package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SpeechRateControl extends LogarithmicFloatControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechRate;
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_SpeechRate;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_SpeechRate;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-rate";
  }

  @Override
  protected float getLinearScale () {
    return super.getLinearScale() / 2.0f;
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationDefaults.SPEECH_RATE;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_RATE;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setRate(value)) return false;
    ApplicationSettings.SPEECH_RATE = value;
    return true;
  }

  public SpeechRateControl () {
    super(ControlGroup.SPEECH);
  }
}
