package org.nbp.common.speech;
import org.nbp.common.*;

import org.nbp.common.controls.CollectionControl;

import android.speech.tts.TextToSpeech;

public abstract class EngineControl extends CollectionControl<TextToSpeech.EngineInfo> {
  private final TextToSpeech tts = new TextToSpeech(CommonContext.getContext(), null);

  public EngineControl () {
    super();

    for (TextToSpeech.EngineInfo info : tts.getEngines()) {
      addCollectionValue(info);
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
  protected final TextToSpeech.EngineInfo getCollectionDefault () {
    return getValue(tts.getDefaultEngine());
  }
}
