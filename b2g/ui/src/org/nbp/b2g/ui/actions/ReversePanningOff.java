package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ReversePanningOff extends PreviousValueAction {
  public ReversePanningOff (Endpoint endpoint) {
    super(endpoint, Controls.getReversePanningControl(), false);
  }
}
