package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleMonitorOff extends PreviousValueAction {
  public BrailleMonitorOff (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleMonitorControl(), false);
  }
}
