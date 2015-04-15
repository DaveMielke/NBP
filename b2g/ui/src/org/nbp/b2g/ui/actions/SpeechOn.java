package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOn extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      ApplicationParameters.SPEECH_ON = true;
      message("speech", ApplicationParameters.SPEECH_ON);
    }

    return true;
  }

  public SpeechOn (Endpoint endpoint) {
    super(endpoint);
  }
}
