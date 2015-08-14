package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOff extends PreviousValueAction {
  public OneHandOff (Endpoint endpoint) {
    super(endpoint, Controls.getOneHandControl(), false);
  }
}
