package org.nbp.ipaws;

import android.util.Log;

public class DenyHandler extends OperandsHandler {
  private final static String LOG_TAG = DenyHandler.class.getName();

  public DenyHandler () {
    super();
  }

  @Override
  public final boolean handleOperands (String string) {
    String log = "access denied";
    string = string.trim();
    if (!string.isEmpty()) log += ": " + string;
    Log.w(LOG_TAG, log);

    Controls.alertMonitor.previousValue();
    return true;
  }
}
