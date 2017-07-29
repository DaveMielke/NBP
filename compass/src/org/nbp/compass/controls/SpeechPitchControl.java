package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.PitchControl;

public class SpeechPitchControl extends PitchControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechPitch;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-pitch";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationDefaults.SPEECH_PITCH;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_PITCH;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!EventAnnouncer.singleton().setPitch(value)) return false;
    ApplicationSettings.SPEECH_PITCH = value;
    return true;
  }

  public SpeechPitchControl () {
    super();
  }
}
