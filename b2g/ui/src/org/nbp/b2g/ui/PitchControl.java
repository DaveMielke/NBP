package org.nbp.b2g.ui;

public class PitchControl extends LogarithmicFloatControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.pitch_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-pitch";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_PITCH;
  }

  @Override
  protected float getFloatValue () {
    return Devices.getSpeechDevice().getPitch();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.getSpeechDevice().setPitch(value);
  }

  public PitchControl () {
    super();
  }
}
