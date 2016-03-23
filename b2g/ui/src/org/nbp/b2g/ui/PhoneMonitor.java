package org.nbp.b2g.ui;

import android.content.Context;

import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class PhoneMonitor extends PhoneStateListener {
  private static TelephonyManager telephonyManager = null;

  private final static Object DATA_LOCK = new Object();
  private static int signalStrength = 99;

  public final static int getSignalStrength () {
    synchronized (DATA_LOCK) {
      return signalStrength;
    }
  }

  @Override
  public void onSignalStrengthsChanged (SignalStrength strength) {
    if (strength.isGsm()) {
      synchronized (DATA_LOCK) {
        int asu = strength.getGsmSignalStrength();
        signalStrength = (asu * 2) - 113;
      }
    }
  }

  public final static void register (Context context) {
    telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

    telephonyManager.listen(
      new PhoneMonitor(),
      PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
    );
  }
}
