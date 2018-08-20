package org.nbp.ipaws;

import android.util.Log;

import android.app.Application;

public class AlertApplication extends Application {
  private final static String LOG_TAG = AlertApplication.class.getName();

  private static AlertApplication alertApplication = null;

  public static AlertApplication getAlertApplication () {
    return alertApplication;
  }

  @Override
  public void onCreate () {
    super.onCreate();
    alertApplication = this;
    Log.d(LOG_TAG, "starting");
  }
}
