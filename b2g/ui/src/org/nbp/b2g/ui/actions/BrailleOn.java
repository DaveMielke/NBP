package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleOn extends NextValueAction {
  public BrailleOn (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleEnabledControl(), false);
  }
}
