package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class StopSpeaking extends Action {
  @Override
  public boolean performAction () {
    return Devices.speech.get().stopSpeaking();
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.StopSpeaking_action_confirmation;
  }

  public StopSpeaking (Endpoint endpoint) {
    super(endpoint, false);
  }
}
