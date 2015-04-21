package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getOneHandControl().nextValue();
  }

  public OneHandOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
