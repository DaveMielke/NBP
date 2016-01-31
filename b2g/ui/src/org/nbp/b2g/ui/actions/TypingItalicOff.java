package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingItalicOff extends PreviousValueAction {
  public TypingItalicOff (Endpoint endpoint) {
    super(endpoint, Controls.getTypingItalicControl(), false);
  }
}
