package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakHigher extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (Controls.getPitchControl().up()) {
        return true;
      }
    }

    return false;
  }

  public SpeakHigher (Endpoint endpoint) {
    super(endpoint);
  }
}
