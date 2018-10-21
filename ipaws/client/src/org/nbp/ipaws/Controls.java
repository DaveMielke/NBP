package org.nbp.ipaws;
import org.nbp.ipaws.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static AlertMonitorControl alertMonitor = new AlertMonitorControl();
  public final static SpeakAlertsControl speakAlerts = new SpeakAlertsControl();
  public final static SpeechEngineControl speechEngine = new SpeechEngineControl();

  public final static PrimaryServerControl primaryServer = new PrimaryServerControl();
  public final static SecondaryServerControl secondaryServer = new SecondaryServerControl();

  public final static Control[] ALL = new Control[] {
    alertMonitor,
    speakAlerts,
    speechEngine,

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
