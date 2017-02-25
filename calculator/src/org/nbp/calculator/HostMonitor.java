package org.nbp.calculator;

import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = HostMonitor.class.getName();

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();

    if (action != null) {
      Log.d(LOG_TAG, "host event: " + action);

      if (action.equals(Intent.ACTION_LOCALE_CHANGED)) {
        ComplexFormatter.resetLocaleData();
      }
    }
  }
}
