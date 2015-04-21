package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getOneHandControl().previousValue();
  }

  public OneHandOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
