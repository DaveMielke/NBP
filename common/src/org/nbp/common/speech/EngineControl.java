package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.Set;
import java.util.HashSet;

import org.nbp.common.controls.CollectionControl;

import android.speech.tts.TextToSpeech;

public abstract class EngineControl extends CollectionControl<TextToSpeech.EngineInfo> {
  private final TextToSpeech tts = new TextToSpeech(CommonContext.getContext(), null);
  private final Set<String> engineNames = new HashSet<String>();

  @Override
  protected void resetItems () {
    super.resetItems();
    engineNames.clear();
  }

  @Override
  protected final void addCollectionValues () {
    for (TextToSpeech.EngineInfo engine : tts.getEngines()) {
      addCollectionValue(engine);
      engineNames.add(engine.name);
    }
  }

  public EngineControl () {
    super();
    addCollectionValues();
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

  @Override
  protected final boolean testCollectionValue (TextToSpeech.EngineInfo value) {
    return engineNames.contains(value.name);
  }
}
