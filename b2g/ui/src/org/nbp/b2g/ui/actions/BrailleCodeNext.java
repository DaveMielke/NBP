package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleCodeNext extends NextValueAction {
  public BrailleCodeNext (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleCodeControl(), false);
  }
}
