package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ActivityInfo;

public class AddShortcut extends ShortcutAction {
  @Override
  protected void performShortcutAction (PackageManager pm, ActivityInfo activity) {
    Intent intent = new Intent();
    intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, newActivityIntent(activity));
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getLabel(pm, activity));
    intent.putExtra("duplicate", false);

    sendIntent(intent);
  }

  public AddShortcut (Endpoint endpoint) {
    super(endpoint);
  }
}
