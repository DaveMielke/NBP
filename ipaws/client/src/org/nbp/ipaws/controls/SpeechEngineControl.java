package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.speech.EngineControl;

import android.speech.tts.TextToSpeech;

public class SpeechEngineControl extends EngineControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechEngine;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-engine";
  }

  @Override
  public TextToSpeech.EngineInfo getObjectValue () {
    return getValue(ApplicationSettings.SPEECH_ENGINE);
  }

  @Override
  protected boolean setObjectValue (TextToSpeech.EngineInfo value) {
    ApplicationSettings.SPEECH_ENGINE = getValueName(value);
    return true;
  }

  public SpeechEngineControl () {
    super();
  }
}
