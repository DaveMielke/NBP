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
    ApplicationUtilities.message(R.string.LogsOn_action_confirmation);

    for (BooleanControl control : controls) {
      control.setValue(true);
    }

    return true;
  }

  public LogsOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
