package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

public class SpeechVolumeControl extends LinearFloatControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechVolume;
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_SpeechVolume;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_SpeechVolume;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-volume";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationDefaults.SPEECH_VOLUME;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_VOLUME;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setVolume(value)) return false;
    ApplicationSettings.SPEECH_VOLUME = value;
    return true;
  }

  public SpeechVolumeControl () {
    super(ControlGroup.SPEECH);
  }
}
