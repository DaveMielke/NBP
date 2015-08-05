package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNone extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.getLogUpdatesControl(),
    Controls.getLogKeysControl(),
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
    ApplicationUtilities.message(R.string.LogNone_action_confirmation);
    Controls.forEachControl(controls, disableControl);
    return true;
  }

  public LogNone (Endpoint endpoint) {
    super(endpoint, true);
  }
}
