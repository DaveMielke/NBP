package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleMonitorOn extends NextValueAction {
  public BrailleMonitorOn (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleMonitorControl(), false);
  }
}
