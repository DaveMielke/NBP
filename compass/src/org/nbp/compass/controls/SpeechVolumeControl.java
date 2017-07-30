package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.speech.VolumeControl;

public class SpeechVolumeControl extends VolumeControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechVolume;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
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
    if (!Announcements.setVolume(value)) return false;
    ApplicationSettings.SPEECH_VOLUME = value;
    Announcements.confirmSetting(R.string.control_label_SpeechVolume, getValue());
    return true;
  }

  public SpeechVolumeControl () {
    super();
  }
}
