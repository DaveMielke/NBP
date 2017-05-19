package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ReversePanningOn extends NextValueAction {
  public ReversePanningOn (Endpoint endpoint) {
    super(endpoint, Controls.reversePanning, false);
  }
}
