package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;

public class AddShortcut extends ShortcutAction {
  @Override
  protected Intent getShortcutIntent () {
    Intent intent = new Intent();
    intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    intent.putExtra("duplicate", false);
    return intent;
  }

  public AddShortcut (Endpoint endpoint) {
    super(endpoint);
  }
}
