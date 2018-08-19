package org.nbp.ipaws;

import android.content.Context;

public class AlertComponent {
  private final Context applicationContext;

  protected AlertComponent (Context context) {
    applicationContext = context.getApplicationContext();
  }

  protected final Context getContext () {
    return applicationContext;
  }
}
