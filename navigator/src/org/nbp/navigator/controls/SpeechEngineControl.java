package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.speech.EngineControl;

import android.speech.tts.TextToSpeech;

public class SpeechEngineControl extends EngineControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechEngine;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-engine";
  }

  @Override
  public TextToSpeech.EngineInfo getCollectionValue () {
    return getValue(ApplicationSettings.SPEECH_ENGINE);
  }

  @Override
  protected boolean setCollectionValue (TextToSpeech.EngineInfo value) {
    ApplicationSettings.SPEECH_ENGINE = getValueName(value);
    Announcements.getAnnouncer().startEngine();
    return true;
  }

  public SpeechEngineControl () {
    super();
  }
}
