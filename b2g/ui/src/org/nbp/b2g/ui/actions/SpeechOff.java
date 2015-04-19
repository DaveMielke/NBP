package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOff extends SpeechAction {
  @Override
  public boolean performAction () {
    return Controls.getSpeechOnControl().setPreviousValue();
  }

  public SpeechOff (Endpoint endpoint) {
    super(endpoint);
  }
}
