package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.KeyEvent;

public class EnterKey extends HostAction {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (!endpoint.isEditable()) {
        return performNodeAction(endpoint.getCurrentNode(), AccessibilityNodeInfo.ACTION_CLICK);
      }
    }

    return super.performAction();
  }

  @Override
  protected String getScanCode () {
    return "ENTER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ENTER;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint, false);
  }
}
