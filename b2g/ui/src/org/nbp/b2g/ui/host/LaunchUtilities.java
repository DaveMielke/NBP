package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;
import android.content.ComponentName;

import android.app.Activity;

import android.net.Uri;
import java.io.File;

public abstract class LaunchUtilities {
  private final static String LOG_TAG = LaunchUtilities.class.getName();

  private static Context getContext () {
    return ApplicationContext.getContext();
  }

  public static Intent toIntent (Class<? extends Activity> activity) {
    Intent intent = new Intent(getContext(), activity);

    intent.addFlags(
      Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    );

    return intent;
  }

  public static void launchActivity (Intent intent) {
    intent.addFlags(
      Intent.FLAG_ACTIVITY_CLEAR_TOP |
      Intent.FLAG_ACTIVITY_SINGLE_TOP |
      Intent.FLAG_ACTIVITY_NEW_TASK
    );

    getContext().startActivity(intent);
  }

  public static void launchActivity (Class<? extends Activity> activity) {
    launchActivity(toIntent(activity));
  }

  public static void launchActivity (ActivityInfo info) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);

    intent.setFlags(
      Intent.FLAG_ACTIVITY_NEW_TASK |
      Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    );

    intent.setComponent(new ComponentName(
      info.applicationInfo.packageName, info.name
    ));

    getContext().startActivity(intent);
  }

  public static void launchActivity (ResolveInfo info) {
    launchActivity(info.activityInfo);
  }

  public static void launchViewer (Uri uri) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(uri);
    LaunchUtilities.launchActivity(intent);
  }

  public static void launchViewer (String uri) {
    launchViewer(Uri.parse(uri));
  }

  public static void launchViewer (int uri) {
    launchViewer(getContext().getResources().getString(uri));
  }

  public static void launchViewer (File file) {
    launchViewer(Uri.fromFile(file));
  }

  private LaunchUtilities () {
  }
}
