package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOn extends SpeechAction {
  @Override
  public boolean performAction () {
    return Controls.getSpeechOnControl().setNextValue();
  }

  public SpeechOn (Endpoint endpoint) {
    super(endpoint);
  }
}
