package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingItalicOn extends NextValueAction {
  public TypingItalicOn (Endpoint endpoint) {
    super(endpoint, Controls.getTypingItalicControl(), false);
  }
}
