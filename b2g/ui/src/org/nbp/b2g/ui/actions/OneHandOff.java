package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getOneHandControl().setPreviousValue();
  }

  public OneHandOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
