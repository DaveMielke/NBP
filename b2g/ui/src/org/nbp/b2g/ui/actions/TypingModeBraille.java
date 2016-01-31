package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingModeBraille extends NextValueAction {
  public TypingModeBraille (Endpoint endpoint) {
    super(endpoint, Controls.getTypingModeControl(), false);
  }
}
