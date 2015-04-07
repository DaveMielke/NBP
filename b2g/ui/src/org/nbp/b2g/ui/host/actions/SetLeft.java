package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetLeft extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      int offset = cursorKey;

      if (offset < 1) return false;
      if (offset >= BrailleDevice.size()) return false;

      if ((offset += endpoint.getLineIndent()) >= endpoint.getLineLength()) return false;
      endpoint.setLineIndent(offset);
      return endpoint.write();
    }
  }

  public SetLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
