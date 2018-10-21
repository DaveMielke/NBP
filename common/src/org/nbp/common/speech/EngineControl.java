package org.nbp.common.speech;
import org.nbp.common.*;

import org.nbp.common.controls.ObjectControl;

import android.speech.tts.TextToSpeech;

public abstract class EngineControl extends ObjectControl<TextToSpeech.EngineInfo> {
  private final TextToSpeech tts = new TextToSpeech(CommonContext.getContext(), null);

  public EngineControl () {
    super();

    for (TextToSpeech.EngineInfo info : tts.getEngines()) {
      addObjectValue(info);
    }
  }

  @Override
  protected final String getValueName (TextToSpeech.EngineInfo info) {
    return info.name;
  }

  @Override
  protected final String getValueLabel (TextToSpeech.EngineInfo info) {
    return info.label;
  }

  @Override
  protected final TextToSpeech.EngineInfo getObjectDefault () {
    return getValue(tts.getDefaultEngine());
  }
}
