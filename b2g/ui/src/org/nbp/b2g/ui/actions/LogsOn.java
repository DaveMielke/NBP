package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogsOn extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.getLogUpdatesControl(),
    Controls.getLogActionsControl(),
    Controls.getLogNavigationControl(),
    Controls.getLogBrailleControl()
  };

  private final static ControlProcessor enableControl = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.nextValue(false);
      return true;
    }
  };

  @Override
  public boolean performAction () {
    ApplicationUtilities.message(R.string.LogsOn_action_confirmation);
    Controls.forEachControl(controls, enableControl);
    return true;
  }

  public LogsOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
