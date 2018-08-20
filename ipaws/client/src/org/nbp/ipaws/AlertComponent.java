package org.nbp.ipaws;

import android.content.Context;

public class AlertComponent {
  protected AlertComponent () {
  }

  public static Context getContext () {
    return AlertApplication.getAlertApplication();
  }
}
