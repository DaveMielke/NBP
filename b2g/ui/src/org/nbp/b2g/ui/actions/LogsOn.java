package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogsOn extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.logUpdates,
    Controls.logActions,
    Controls.logNavigation,
    Controls.logBraille
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
    return R.string.LogsOn_action_confirmation;
  }

  public LogsOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
