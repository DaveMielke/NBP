package org.nbp.ipaws;
import org.nbp.ipaws.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static AlertMonitorControl alertMonitor = new AlertMonitorControl();

  public final static PrimaryServerControl primaryServer = new PrimaryServerControl();
  public final static SecondaryServerControl secondaryServer = new SecondaryServerControl();

  public final static Control[] ALL = new Control[] {
    alertMonitor,

    primaryServer,
    secondaryServer
  };

  private static boolean RESTORE = true;

  public final static void restore () {
    synchronized (ALL) {
      if (RESTORE) {
        RESTORE = false;

        for (Control control : ALL) {
          if (control == alertMonitor) continue;

          control.restoreCurrentValue();
        }

        alertMonitor.restoreCurrentValue();
        AlertNotification.create();
      }
    }
  }
}
