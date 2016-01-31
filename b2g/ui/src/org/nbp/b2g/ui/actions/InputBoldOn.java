package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputBoldOn extends NextValueAction {
  public InputBoldOn (Endpoint endpoint) {
    super(endpoint, Controls.getInputBoldControl(), false);
  }
}
