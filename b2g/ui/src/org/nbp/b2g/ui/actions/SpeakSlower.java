package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakSlower extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
    }

    return false;
  }

  public SpeakSlower (Endpoint endpoint) {
    super(endpoint);
  }
}
