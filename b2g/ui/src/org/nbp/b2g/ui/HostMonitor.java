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
import android.os.BatteryManager;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = HostMonitor.class.getName();

  public static boolean haveBattery (Bundle battery) {
    return battery.getBoolean(BatteryManager.EXTRA_PRESENT, true);
  }

  public static int getBatteryPercentage (Bundle battery) {
    if (haveBattery(battery)) {
      int scale = battery.getInt(BatteryManager.EXTRA_SCALE, 0);

      if (scale > 0) {
        int level = battery.getInt(BatteryManager.EXTRA_LEVEL, 0);
        return (level * 100) / scale;
      }
    }

    return -1;
  }

  private final static Map<String, Bundle> intentExtras = new HashMap<String, Bundle>();

  private static Bundle getIntentExtras (String action) {
    synchronized (intentExtras) {
      return intentExtras.get(action);
    }
  }

  public static Bundle getBatteryStatus () {
    return getIntentExtras(Intent.ACTION_BATTERY_CHANGED);
  }

  private final static String sdcardPath;
  private static PowerManager.WakeLock sdcardWakeLock = null;

  static {
    String path = System.getenv("SECONDARY_STORAGE");
    if (path == null) path = System.getenv("EXTERNAL_STORAGE");

    sdcardPath = path;
    Log.d(LOG_TAG, "SD card path: " + sdcardPath);
  }

  private enum MediaAction {
    ADDED,
    REMOVED
  }

  private static void handleMediaAction (Intent intent, MediaAction action) {
    String path = intent.getData().getPath();
    Log.d(LOG_TAG, "media path: " + path);

    if (path.equals(sdcardPath)) {
      synchronized (sdcardPath) {
        boolean haveWakeLock = sdcardWakeLock != null;

        switch (action) {
          case ADDED: {
            if (!haveWakeLock) {
              sdcardWakeLock = ApplicationContext.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "sdcard"
              );
            }

            if (!sdcardWakeLock.isHeld()) {
              Log.d(LOG_TAG, "acquiring SD card wake lock");
              sdcardWakeLock.acquire();
            }

            Characters.setCharacters(new Characters());
            break;
          }

          case REMOVED: {
            if (haveWakeLock && sdcardWakeLock.isHeld()) {
              Log.d(LOG_TAG, "releasing SD card wake lock");
              sdcardWakeLock.release();
            }

            break;
          }
        }
      }
    }
  }

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();
    if (action == null) return;
    Log.d(LOG_TAG, "host event: " + action);

    if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
      handleMediaAction(intent, MediaAction.ADDED);
      return;
    }

    if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
      handleMediaAction(intent, MediaAction.REMOVED);
      return;
    }

    if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
      Log.d(LOG_TAG, "starting screen monitor");
      org.nbp.b2g.ui.host.ScreenMonitor.start();

      Log.d(LOG_TAG, "starting input service");
      InputService.start();

      return;
    }

    synchronized (intentExtras) {
      Bundle extras = intent.getExtras();

      if (intentExtras.get(action) == null) {
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
          int percentage = getBatteryPercentage(extras);

          if (percentage >= 0) {
            ApplicationUtilities.message(
              String.format(
                "%s %d%%",
                context.getString(R.string.message_battery_percentage),
                percentage
              )
            );
          }
        }
      }

      intentExtras.put(action, extras);
    }
  }

  public static void start (Context context) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_BATTERY_CHANGED);

    BroadcastReceiver receiver = new HostMonitor();
    context.registerReceiver(receiver, filter);
  }
}
