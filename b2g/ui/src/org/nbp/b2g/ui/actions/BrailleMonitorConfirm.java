package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleMonitorConfirm extends ConfirmValueAction {
  public BrailleMonitorConfirm (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleMonitorControl(), false);
  }
}
