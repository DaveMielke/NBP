package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SpeechRateControl extends LogarithmicFloatControl {
  @Override
  public int getLabel () {
    return R.string.control_label_SpeechRate;
  }

  @Override
  public CharSequence getNextLabel () {
    return getString(R.string.control_next_SpeechRate);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getString(R.string.control_previous_SpeechRate);
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
