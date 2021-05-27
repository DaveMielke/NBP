package org.nbp.common.speech;
import org.nbp.common.*;

import java.util.List;

import android.speech.tts.TextToSpeech;

public abstract class TTS {
  private final static TextToSpeech tts = new TextToSpeech(CommonContext.getContext(), null);

  public static List<TextToSpeech.EngineInfo> getEngines () {
    return tts.getEngines();
  }

  public static String getDefaultEngine () {
    return tts.getDefaultEngine();
  }

  private TTS () {
  }
}
