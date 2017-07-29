package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.RateControl;

public class SpeechRateControl extends RateControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechRate;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-rate";
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
    if (!EventAnnouncer.singleton().setRate(value)) return false;
    ApplicationSettings.SPEECH_RATE = value;
    return true;
  }

  public SpeechRateControl () {
    super();
  }
}
