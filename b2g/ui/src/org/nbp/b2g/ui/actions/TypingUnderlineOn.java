package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingUnderlineOn extends NextValueAction {
  public TypingUnderlineOn (Endpoint endpoint) {
    super(endpoint, Controls.getTypingUnderlineControl(), false);
  }
}
