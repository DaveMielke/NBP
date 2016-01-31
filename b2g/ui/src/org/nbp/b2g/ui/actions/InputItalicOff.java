package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputItalicOff extends PreviousValueAction {
  public InputItalicOff (Endpoint endpoint) {
    super(endpoint, Controls.getInputItalicControl(), false);
  }
}
