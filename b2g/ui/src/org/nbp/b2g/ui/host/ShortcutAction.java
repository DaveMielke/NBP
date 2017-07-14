package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import org.nbp.common.LaunchUtilities;

import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.res.Resources;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class ShortcutAction extends Action {
  protected abstract Intent getShortcutIntent ();

  protected static PackageManager getPackageManager () {
    return getContext().getPackageManager();
  }

  protected static String getLabel (PackageManager pm, ActivityInfo activity) {
    return activity.loadLabel(pm).toString();
  }

  protected static Bitmap getIcon (PackageManager pm, ActivityInfo activity) {
    int resource = activity.getIconResource();

    if (resource != 0) {
      ApplicationInfo application = activity.applicationInfo;

      try {
        Resources resources = pm.getResourcesForApplication(application);
        return BitmapFactory.decodeResource(resources, resource);
      } catch (PackageManager.NameNotFoundException exception) {
      }
    }

    return null;
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

    Bitmap icon = getIcon(pm, activity);
    if (icon != null) intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);

    sendIntent(intent);
  }

  protected final String getText () {
    return getEndpoint().getLineText().toString();
  }

  @Override
  public boolean performAction () {
    String text = getText().toLowerCase();
    final PackageManager pm = getPackageManager();
    List<ResolveInfo> activities = LaunchUtilities.getLaunchableActivities(pm);
    final List<ActivityInfo> choices = new ArrayList<ActivityInfo>();

    for (ResolveInfo resolve : activities) {
      ActivityInfo activity = resolve.activityInfo;
      String label = getLabel(pm, activity);
      if (label == null) continue;

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
          new PopupClickHandler () {
            @Override
            public final boolean handleClick (int index) {
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
