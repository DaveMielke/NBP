package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.ComponentName;

public class SwitchLauncher extends Action {
  @Override
  public boolean performAction () {
    Context context = getContext();
    if (context == null) return false;

    ComponentName fakeLauncher = new ComponentName(context, FakeLauncher.class);
    PackageManager packageManager = context.getPackageManager();

    packageManager.setComponentEnabledSetting(
      fakeLauncher,
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
      PackageManager.DONT_KILL_APP
    );

    Intent chooser = new Intent(Intent.ACTION_MAIN);
    chooser.addCategory(Intent.CATEGORY_HOME);
    chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(chooser);

    packageManager.setComponentEnabledSetting(
      fakeLauncher,
      PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
      PackageManager.DONT_KILL_APP
    );

    return true;
  }

  public SwitchLauncher (Endpoint endpoint) {
    super(endpoint, true);
  }
}
