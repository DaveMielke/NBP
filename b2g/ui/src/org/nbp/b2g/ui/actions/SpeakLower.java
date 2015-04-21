package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLower extends SpeechAction {
  @Override
  public boolean performAction () {
    SpeechDevice speech = getSpeechDevice();

    synchronized (speech) {
      if (Controls.getSpeechPitchControl().previousValue()) {
        return true;
      }
    }

    return false;
  }

  public SpeakLower (Endpoint endpoint) {
    super(endpoint);
  }
}
