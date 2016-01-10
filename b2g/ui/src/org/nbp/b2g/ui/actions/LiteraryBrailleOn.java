package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LiteraryBrailleOn extends NextValueAction {
  public LiteraryBrailleOn (Endpoint endpoint) {
    super(endpoint, Controls.getLiteraryBrailleControl(), false);
  }
}
