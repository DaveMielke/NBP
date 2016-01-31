package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputItalicOn extends NextValueAction {
  public InputItalicOn (Endpoint endpoint) {
    super(endpoint, Controls.getInputItalicControl(), false);
  }
}
