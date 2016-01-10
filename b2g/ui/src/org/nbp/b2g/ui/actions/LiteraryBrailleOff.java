package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LiteraryBrailleOff extends PreviousValueAction {
  public LiteraryBrailleOff (Endpoint endpoint) {
    super(endpoint, Controls.getLiteraryBrailleControl(), false);
  }
}
