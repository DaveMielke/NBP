package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class StopSpeaking extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (!speech.stopSpeaking()) return false;
    }

    ApplicationUtilities.message(R.string.stopSpeaking_action_confirmation);
    return true;
  }

  public StopSpeaking (Endpoint endpoint) {
    super(endpoint);
  }
}
