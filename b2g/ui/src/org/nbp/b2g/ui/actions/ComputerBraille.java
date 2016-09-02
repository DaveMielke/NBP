package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ComputerBraille extends PreviousValueAction {
  public ComputerBraille (Endpoint endpoint) {
    super(endpoint, Controls.getLiteraryBrailleControl(), false);
  }
}
