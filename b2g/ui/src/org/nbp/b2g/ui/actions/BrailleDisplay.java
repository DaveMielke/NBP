package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleDisplay extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setDisplayEndpoint();
    return true;
  }

  public BrailleDisplay (Endpoint endpoint) {
    super(endpoint, false);
  }
}
