package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleCodePrevious extends PreviousValueAction {
  public BrailleCodePrevious (Endpoint endpoint) {
    super(endpoint, Controls.getBrailleCodeControl(), false);
  }
}
