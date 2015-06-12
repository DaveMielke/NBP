package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PanLeft extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().panLeft();
  }

  public PanLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
