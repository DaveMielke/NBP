package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class StopSpeaking extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      return speech.stopSpeaking();
    }
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.StopSpeaking_action_confirmation;
  }

  public StopSpeaking (Endpoint endpoint) {
    super(endpoint);
  }
}
