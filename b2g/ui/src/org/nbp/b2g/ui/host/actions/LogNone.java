package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogNone extends Action {
  private final static BooleanControl[] controls = new BooleanControl[] {
    Controls.getLogKeysControl(),
    Controls.getLogActionsControl(),
    Controls.getLogNavigationControl(),
    Controls.getLogUpdatesControl(),
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
    Controls.forEachControl(controls, disableControl);
    message("Logs Off");
    return true;
  }

  public LogNone (Endpoint endpoint) {
    super(endpoint, true);
  }
}
