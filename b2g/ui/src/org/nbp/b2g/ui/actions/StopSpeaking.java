package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class StopSpeaking extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    if (speech.stopSpeaking()) {
      ApplicationUtilities.message(R.string.stopSpeaking_action_confirmation);
      return true;
    }

    return false;
  }

  public StopSpeaking (Endpoint endpoint) {
    super(endpoint);
  }
}
