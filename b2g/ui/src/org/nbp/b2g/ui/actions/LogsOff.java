package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogsOff extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.getLogUpdatesControl(),
    Controls.getLogKeyboardControl(),
    Controls.getLogActionsControl(),
    Controls.getLogNavigationControl(),
    Controls.getLogGesturesControl(),
    Controls.getLogBrailleControl()
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
