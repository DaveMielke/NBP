package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.PowerManager;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = HostMonitor.class.getName();

  private final static Map<String, Bundle> intentExtras = new HashMap<String, Bundle>();

  private final static String sdcardPath;
  private static PowerManager.WakeLock sdcardWakeLock = null;

  static {
    String path = System.getenv("SECONDARY_STORAGE");
    if (path == null) path = System.getenv("EXTERNAL_STORAGE");

    sdcardPath = path;
    Log.d(LOG_TAG, "SD card path: " + sdcardPath);
  }

  private static Bundle getIntentExtras (String action) {
    synchronized (intentExtras) {
      return intentExtras.get(action);
    }
  }

  public static Bundle getBatteryStatus () {
    return getIntentExtras(Intent.ACTION_BATTERY_CHANGED);
  }

  public static void changeMediaState (Intent intent, boolean added) {
    String path = intent.getData().getPath();
    Log.d(LOG_TAG, "media path: " + path);

    if (path.equals(sdcardPath)) {
      synchronized (sdcardPath) {
        boolean haveWakeLock = sdcardWakeLock != null;

        if (added) {
          if (!haveWakeLock) {
            sdcardWakeLock = ApplicationContext.getPowerManager().newWakeLock(
              PowerManager.PARTIAL_WAKE_LOCK,
              ApplicationContext.getString(R.string.app_name)
            );
          }

          sdcardWakeLock.acquire();
        } else if (haveWakeLock) {
          sdcardWakeLock.release();
        }
      }
    }
  }

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();

    if (action != null) {
      Log.d(LOG_TAG, "host event: " + action);

      if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
        changeMediaState(intent, true);
        Characters.setCharacters(new Characters());
        return;
      }

      if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
        changeMediaState(intent, false);
        return;
      }

      synchronized (intentExtras) {
        intentExtras.put(action, intent.getExtras());
      }
    }
  }

  public static void monitorEvents (Context context) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_BATTERY_CHANGED);

    BroadcastReceiver receiver = new HostMonitor();
    context.registerReceiver(receiver, filter);
  }
}
