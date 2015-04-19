package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakRight extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (Controls.getSpeechBalanceControl().setNextValue()) {
        return true;
      }
    }

    return false;
  }

  public SpeakRight (Endpoint endpoint) {
    super(endpoint);
  }
}
