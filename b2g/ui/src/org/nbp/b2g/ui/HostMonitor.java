package org.nbp.b2g.ui;

import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class HostMonitor extends BroadcastReceiver {
  private final static String LOG_TAG = HostMonitor.class.getName();

  @Override
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();

    if (action != null) {
      Log.d(LOG_TAG, "host action: " + action);

      if (Environment.isExternalStorageRemovable()) {
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
          Characters.setCharacters(new Characters());
        }
      }
    }
  }
}
