package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class StopSpeaking extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (speech.stopSpeaking()) {
        message("speech stopped");
        return true;
      }
    }

    return false;
  }

  public StopSpeaking (Endpoint endpoint) {
    super(endpoint);
  }
}
