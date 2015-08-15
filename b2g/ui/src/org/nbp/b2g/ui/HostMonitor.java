package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Environment;
import android.os.Bundle;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = HostMonitor.class.getName();

  private final static Map<String, Bundle> intentExtras = new HashMap<String, Bundle>();

  private static Bundle getIntentExtras (String action) {
    synchronized (intentExtras) {
      return intentExtras.get(action);
    }
  }

  public static Bundle getBatteryStatus () {
    return getIntentExtras(Intent.ACTION_BATTERY_CHANGED);
  }

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();

    if (action != null) {
      Log.d(LOG_TAG, "host event: " + action);

      if (Environment.isExternalStorageRemovable()) {
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
          Characters.setCharacters(new Characters());
          return;
        }
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
