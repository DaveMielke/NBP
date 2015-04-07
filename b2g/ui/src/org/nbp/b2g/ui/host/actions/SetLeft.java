package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetLeft extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      return endpoint.scrollRight(cursorKey);
    }
  }

  public SetLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
