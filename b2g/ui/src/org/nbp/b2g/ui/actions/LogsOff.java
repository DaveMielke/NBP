package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class LogsOff extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.logUpdates,
    Controls.logKeyboard,
    Controls.logActions,
    Controls.logNavigation,
    Controls.logGestures,
    Controls.logBraille,
    Controls.logSpeech
  };

  @Override
  public boolean performAction () {
    for (BooleanControl control : controls) {
      control.setValue(false);
    }

    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.LogsOff_action_confirmation;
  }

  public LogsOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
