package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.ScreenMonitor;

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

  private final static Map<String, Bundle> actionExtras =
               new HashMap<String, Bundle>();

  public final static Bundle getActionExtras (String action) {
    synchronized (actionExtras) {
      return actionExtras.get(action);
    }
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

  private interface ActionPerformer {
    public void performAction (Context context, Intent intent, boolean first);
  }

  private final static Map<String, ActionPerformer> actionPerformers =
               new HashMap<String, ActionPerformer>()
  {
    {
      put(
        Intent.ACTION_BOOT_COMPLETED,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            Log.d(LOG_TAG, "starting screen monitor");
            ScreenMonitor.start();

            Log.d(LOG_TAG, "starting input service");
            InputService.start();
          }
        }
      );

      put(
        Intent.ACTION_SCREEN_ON,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            ApplicationUtilities.runOnMainThread(
              new Runnable() {
                @Override
                public void run () {
                  KeyEvents.resetKeys();
                  Controls.restoreSaneValues();
                }
              }
            );
          }
        }
      );

      put(
        Intent.ACTION_BATTERY_CHANGED,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            if (first) {
              BatteryReport.start();

              BatteryProperties battery = new BatteryProperties();
              Double percentage = battery.getPercentFull();

              if (percentage != null) {
                ApplicationUtilities.message(
                  String.format(
                    "%s %.0f%%",
                    context.getString(R.string.message_battery_percentage),
                    percentage
                  )
                );
              }
            }
          }
        }
      );

      put(
        Intent.ACTION_LOCALE_CHANGED,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            ComputerBraille.LOCAL.reloadCharacters();
          }
        }
      );

      put(
        Intent.ACTION_MEDIA_MOUNTED,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            handleMediaAction(intent, MediaAction.ADDED);
          }
        }
      );

      put(
        Intent.ACTION_MEDIA_EJECT,
        new ActionPerformer() {
          @Override
          public void performAction (Context context, Intent intent, boolean first) {
            handleMediaAction(intent, MediaAction.REMOVED);
          }
        }
      );
    }
  };

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();
    if (action == null) return;
    Log.d(LOG_TAG, ("host event: " + action));

    synchronized (actionExtras) {
      ActionPerformer actionPerformer = actionPerformers.get(action);
      Bundle intentExtras = intent.getExtras();

      boolean first = !actionExtras.containsKey(action);
      actionExtras.put(action, intentExtras);

      if (actionPerformer != null) {
        actionPerformer.performAction(context, intent, first);
      }
    }
  }

  public static void start (Context context) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    filter.addAction(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);

    BroadcastReceiver receiver = new HostMonitor();
    context.registerReceiver(receiver, filter);
  }
}
