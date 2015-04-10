package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetLeft extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      return endpoint.scrollRight(cursorKey);
    }
  }

  public SetLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
