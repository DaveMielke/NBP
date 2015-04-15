package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOff extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      ApplicationParameters.SPEECH_ON = false;
      message("speech", ApplicationParameters.SPEECH_ON);
    }

    return true;
  }

  public SpeechOff (Endpoint endpoint) {
    super(endpoint);
  }
}
