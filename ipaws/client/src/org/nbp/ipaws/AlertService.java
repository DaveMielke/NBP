package org.nbp.ipaws;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;

public class AlertService extends Service {
  private static AlertService alertService = null;
  private static SessionThread sessionThread = null;

  public static AlertService getAlertService () {
    return alertService;
  }

  public static SessionThread getSessionThread () {
    return sessionThread;
  }

  @Override
  public void onCreate () {
    super.onCreate();
    alertService = this;

    sessionThread = new SessionThread(this);
    sessionThread.start();
  }

  @Override
  public void onDestroy () {
    try {
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
}
