package org.nbp.common;

import android.util.Log;

import android.app.Application;

public abstract class CommonApplication extends Application {
  private final static String LOG_TAG = CommonApplication.class.getName();

  @Override
  public void onCreate () {
    super.onCreate();
    CommonContext.setContext(this);
  }
}
