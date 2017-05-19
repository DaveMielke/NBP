package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleOff extends PreviousValueAction {
  public BrailleOff (Endpoint endpoint) {
    super(endpoint, Controls.brailleEnabled, false);
  }
}
