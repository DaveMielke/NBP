package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakSofter extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
    }

    return false;
  }

  public SpeakSofter (Endpoint endpoint) {
    super(endpoint);
  }
}
