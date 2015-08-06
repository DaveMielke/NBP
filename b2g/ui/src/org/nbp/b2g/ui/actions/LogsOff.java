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

  private final static ControlProcessor disableControl = new ControlProcessor() {
    @Override
    public boolean processControl (Control control) {
      control.previousValue(false);
      return true;
    }
  };

  @Override
  public boolean performAction () {
    ApplicationUtilities.message(R.string.LogsOff_action_confirmation);
    Controls.forEachControl(controls, disableControl);
    return true;
  }

  public LogsOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
