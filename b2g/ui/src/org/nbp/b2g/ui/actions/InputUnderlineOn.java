package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputUnderlineOn extends NextValueAction {
  public InputUnderlineOn (Endpoint endpoint) {
    super(endpoint, Controls.getInputUnderlineControl(), false);
  }
}
