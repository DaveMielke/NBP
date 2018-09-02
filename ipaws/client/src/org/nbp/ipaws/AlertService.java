package org.nbp.ipaws;

import android.util.Log;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.content.Context;
import org.nbp.common.CommonContext;

public class AlertService extends Service {
  private final static String LOG_TAG = AlertService.class.getName();

  private static AlertService alertService = null;
  private static ServerSession serverSession = null;

  public static AlertService getAlertService () {
    return alertService;
  }

  public static ServerSession getServerSession () {
    return serverSession;
  }

  @Override
  public void onCreate () {
    super.onCreate();
    alertService = this;
    CommonContext.setContext(this);
    AlertNotification.create(this);

    Log.d(LOG_TAG, "starting");
    serverSession = new ServerSession();
  }

  @Override
  public void onDestroy () {
    try {
      Log.d(LOG_TAG, "stopping");

      serverSession.endSession();
      serverSession = null;

      AlertPlayer.stop();

      alertService = null;
    } finally {
      super.onDestroy();
    }
  }

  @Override
  public int onStartCommand (Intent intent, int flags, int identifier) {
    return START_STICKY;
  }

  @Override
  public IBinder onBind (Intent intent) {
    return null;
  }

  public static Intent makeIntent (Context context) {
    return new Intent(context, AlertService.class);
  }
}
