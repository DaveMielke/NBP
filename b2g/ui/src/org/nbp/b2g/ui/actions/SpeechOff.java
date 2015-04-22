package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOff extends SpeechAction {
  @Override
  public boolean performAction () {
    getSpeechDevice().stopSpeaking();
    return Controls.getSpeechOnControl().previousValue();
  }

  public SpeechOff (Endpoint endpoint) {
    super(endpoint);
  }
}
