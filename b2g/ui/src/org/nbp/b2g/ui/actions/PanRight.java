package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PanRight extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().panRight();
  }

  public PanRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
