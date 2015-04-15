package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLouder extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (adjustVolume(speech, 1)) {
        return true;
      }
    }

    return false;
  }

  public SpeakLouder (Endpoint endpoint) {
    super(endpoint);
  }
}
