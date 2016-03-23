package org.nbp.b2g.ui;

import android.content.Context;

import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class PhoneMonitor extends PhoneStateListener {
  private static TelephonyManager telephonyManager = null;

  private final static Object DATA_LOCK = new Object();
  private static int signalStrength = 0;

  public final static int getSignalStrength () {
    synchronized (DATA_LOCK) {
      return signalStrength;
    }
  }

  @Override
  public void onSignalStrengthsChanged (SignalStrength strength) {
    synchronized (DATA_LOCK) {
      if (strength.isGsm()) {
        int asu = strength.getGsmSignalStrength();

        if (asu == 99) {
          signalStrength = 0;
        } else {
          signalStrength = (asu * 100) / 31;
        }
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
