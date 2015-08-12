package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogsOn extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.getLogUpdatesControl(),
    Controls.getLogActionsControl(),
    Controls.getLogNavigationControl(),
    Controls.getLogBrailleControl()
  };

  @Override
  public boolean performAction () {
    for (BooleanControl control : controls) {
      control.setValue(true);
    }

    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.LogsOff_action_confirmation;
  }

  public LogsOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
