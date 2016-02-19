package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingStrikeOff extends PreviousValueAction {
  public TypingStrikeOff (Endpoint endpoint) {
    super(endpoint, Controls.getTypingStrikeControl(), false);
  }
}
