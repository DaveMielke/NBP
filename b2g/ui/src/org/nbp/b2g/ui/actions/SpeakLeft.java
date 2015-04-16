package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLeft extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (Controls.getBalanceControl().down()) {
        return true;
      }
    }

    return false;
  }

  public SpeakLeft (Endpoint endpoint) {
    super(endpoint);
  }
}
