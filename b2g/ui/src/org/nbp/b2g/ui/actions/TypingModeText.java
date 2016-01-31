package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingModeText extends PreviousValueAction {
  public TypingModeText (Endpoint endpoint) {
    super(endpoint, Controls.getTypingModeControl(), false);
  }
}
