package org.nbp.common;

import java.util.List;

import android.util.Log;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ActivityInfo;
import android.content.ComponentName;

import android.app.Activity;
import android.content.ActivityNotFoundException;

import android.net.Uri;
import java.io.File;

public abstract class LaunchUtilities {
  private final static String LOG_TAG = LaunchUtilities.class.getName();

  private static Context getContext () {
    return CommonContext.getContext();
  }

  public static List<ResolveInfo> getLaunchableActivities (PackageManager pm) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    return pm.queryIntentActivities(intent, 0);
  }

  public static Intent toIntent (Class<? extends Activity> activity) {
    Intent intent = new Intent(getContext(), activity);

    intent.addFlags(
      Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
    );

    return intent;
  }

  public static ComponentName toComponentName (ActivityInfo info) {
    return new ComponentName(info.applicationInfo.packageName, info.name);
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

  public static void launchActivity (ComponentName name) {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.setComponent(name);

    intent.setFlags(
      Intent.FLAG_ACTIVITY_NEW_TASK |
      Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    );

    getContext().startActivity(intent);
  }

  public static void launchActivity (String packageName, String activityClass) {
    launchActivity(new ComponentName(packageName, activityClass));
  }

  public static void launchActivity (ActivityInfo info) {
    launchActivity(toComponentName(info));
  }

  public static void launchActivity (ResolveInfo info) {
    launchActivity(info.activityInfo);
  }

  public static boolean launchActivity (Intent intent, int title, String... packages) {
    Context context = getContext();
    PackageManager pm = context.getPackageManager();
    List<ResolveInfo> choices = pm.queryIntentActivities(intent, 0);

    for (String name : packages) {
      for (ResolveInfo choice : choices) {
        if (name.equals(choice.activityInfo.applicationInfo.packageName)) {
          intent.setComponent(toComponentName(choice.activityInfo));
          launchActivity(intent);
          return true;
        }
      }
    }

    Intent chooser = Intent.createChooser(intent, context.getResources().getString(title));
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    try {
      context.startActivity(chooser);
      return true;
    } catch (ActivityNotFoundException exception) {
    }

    return false;
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
