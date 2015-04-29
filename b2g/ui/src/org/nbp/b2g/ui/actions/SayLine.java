package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayLine extends SpeechAction {
  @Override
  public boolean performAction () {
    String line;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        line = endpoint.getLineText();
      }
    }

    SpeechDevice speech = getSpeechDevice();
    synchronized (speech) {
      speech.stopSpeaking();
      return speech.say(line);
    }
  }

  public SayLine (Endpoint endpoint) {
    super(endpoint);
  }
}
