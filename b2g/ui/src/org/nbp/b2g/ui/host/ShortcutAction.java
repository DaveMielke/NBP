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
  protected abstract Intent getShortcutIntent ();

  protected static Context getContext () {
    return ApplicationContext.getContext();
  }

  protected static PackageManager getPackageManager () {
    return getContext().getPackageManager();
  }

  protected static String getLabel (PackageManager pm, ActivityInfo activity) {
    return pm.getApplicationLabel(activity.applicationInfo).toString();
  }

  protected static void sendIntent (Intent intent) {
    getContext().sendBroadcast(intent);
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

  protected final void performShortcutAction (PackageManager pm, ActivityInfo activity) {
    Intent intent = getShortcutIntent();

    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, newActivityIntent(activity));
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getLabel(pm, activity));

    sendIntent(intent);
  }

  protected final String getText () {
    return getEndpoint().getLineText().toString();
  }

  protected static List<ResolveInfo> getLaunchableActivities (PackageManager pm) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    return pm.queryIntentActivities(intent, 0);
  }

  @Override
  public boolean performAction () {
    String text = getText().toLowerCase();
    final PackageManager pm = getPackageManager();
    List<ResolveInfo> activities = getLaunchableActivities(pm);
    final List<ActivityInfo> choices = new ArrayList<ActivityInfo>();

    for (ResolveInfo resolve : activities) {
      ActivityInfo activity = resolve.activityInfo;
      if (activity.targetActivity != null) continue;
      String label = getLabel(pm, activity);

      if (label.toLowerCase().contains(text)) {
        choices.add(activity);
      }
    }

    switch (choices.size()) {
      case 0:
        ApplicationUtilities.message(R.string.shortcut_message_none);
        return false;

      case 1:
        performShortcutAction(pm, choices.get(0));
        break;

      default: {
        StringBuilder sb = new StringBuilder();
        sb.append(ApplicationContext.getString(R.string.shortcut_message_ambiguous));

        for (ActivityInfo activity : choices) {
          sb.append('\n');
          sb.append(getLabel(pm, activity));
        }

        Endpoints.setPopupEndpoint(sb.toString(),
          new ValueHandler<Integer> () {
            @Override
            public boolean handleValue (Integer index) {
              if ((index -= 1) < 0) return true;
              ActivityInfo activity = choices.get(index);

              performShortcutAction(pm, activity);
              return true;
            }
          }
        );

        break;
      }
    }

    return true;
  }

  protected ShortcutAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
