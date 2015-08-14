package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayLine extends Action {
  @Override
  public boolean performAction () {
    CharSequence line;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        line = endpoint.getLineText();
      }
    }

    SpeechDevice speech = Devices.speech.get();
    synchronized (speech) {
      speech.stopSpeaking();
      return speech.say(line);
    }
  }

  public SayLine (Endpoint endpoint) {
    super(endpoint, false);
  }
}
