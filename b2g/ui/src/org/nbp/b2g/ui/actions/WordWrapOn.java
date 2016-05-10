package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class WordWrapOn extends NextValueAction {
  public WordWrapOn (Endpoint endpoint) {
    super(endpoint, Controls.getWordWrapControl(), false);
  }
}
