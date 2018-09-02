package org.nbp.ipaws;

import android.util.Log;

import org.nbp.common.CommonContext;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = MainActivity.class.getName();

  private final void startAlertService (Context context) {
    context.startService(AlertService.makeIntent(context));
  }

  @Override
  public void onReceive (Context context, Intent intent) {
    CommonContext.setContext(context);

    String action = intent.getAction();
    if (action == null) return;
    Log.d(LOG_TAG, ("host event: " + action));

    if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
      startAlertService(context);
      return;
    }

    if (action.equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
      startAlertService(context);
      return;
    }
  }
}
