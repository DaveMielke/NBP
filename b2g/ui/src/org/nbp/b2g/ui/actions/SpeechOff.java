package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOff extends PreviousValueAction {
  @Override
  public boolean performAction () {
    if (!Devices.speech.get().stopSpeaking()) return false;
    return super.performAction();
  }

  public SpeechOff (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechEnabledControl(), false);
  }
}
