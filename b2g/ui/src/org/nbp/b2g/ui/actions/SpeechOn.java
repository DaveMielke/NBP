package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOn extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
    }

    return false;
  }

  public SpeechOn (Endpoint endpoint) {
    super(endpoint);
  }
}
