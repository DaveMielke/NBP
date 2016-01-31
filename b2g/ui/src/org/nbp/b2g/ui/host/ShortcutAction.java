package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;

public abstract class ShortcutAction extends Action {
  protected final String getText () {
    return getEndpoint().getLineText().toString();
  }

  protected static Context getContext () {
    return ApplicationContext.getContext();
  }

  protected static PackageManager getPackageManager () {
    return getContext().getPackageManager();
  }

  protected static String getLabel (PackageManager pm, ActivityInfo activity) {
    return pm.getApplicationLabel(activity.applicationInfo).toString();
  }

  protected static ActivityInfo findActivity (PackageManager pm, String text) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

    text = text.toLowerCase();
    List<ActivityInfo> choices = new ArrayList<ActivityInfo>();

    for (ResolveInfo resolve : activities) {
      ActivityInfo activity = resolve.activityInfo;
      String label = getLabel(pm, activity);

      if (label.toLowerCase().contains(text)) {
        choices.add(activity);
      }
    }

    switch (choices.size()) {
      case 0:
        ApplicationUtilities.message(R.string.shortcut_message_none);
        break;

      case 1:
        return choices.get(0);

      default: {
        StringBuilder sb = new StringBuilder();
        sb.append(ApplicationContext.getString(R.string.shortcut_message_ambiguous));

        for (ActivityInfo activity : choices) {
          sb.append('\n');
          sb.append(getLabel(pm, activity));
        }

        Endpoints.setPopupEndpoint(sb.toString());
        break;
      }
    }

    return null;
  }

  protected static ComponentName toComponentName (ActivityInfo activity) {
    return new ComponentName(activity.applicationInfo.packageName, activity.name);
  }

  protected static Intent newActivityIntent (ActivityInfo activity) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.setComponent(toComponentName(activity));
    return intent;
  }

  protected static void sendIntent (Intent intent) {
    getContext().sendBroadcast(intent);
  }

  protected ShortcutAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
