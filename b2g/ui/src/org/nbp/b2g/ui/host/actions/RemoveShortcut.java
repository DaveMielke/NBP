package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class RemoveShortcut extends ShortcutAction {
  @Override
  protected Intent getShortcutIntent () {
    Intent intent = new Intent();
    intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
    return intent;
  }

  public RemoveShortcut (Endpoint endpoint) {
    super(endpoint);
  }
}
