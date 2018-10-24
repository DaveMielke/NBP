package org.nbp.ipaws;
import org.nbp.ipaws.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  // general settings
  public final static AlertMonitorControl alertMonitor = new AlertMonitorControl();
  public final static ShowAlertsControl showAlerts = new ShowAlertsControl();
  public final static SpeakAlertsControl speakAlerts = new SpeakAlertsControl();
  public final static SpeechEngineControl speechEngine = new SpeechEngineControl();

  // developer settings
  public final static PrimaryServerControl primaryServer = new PrimaryServerControl();
  public final static SecondaryServerControl secondaryServer = new SecondaryServerControl();

  static {
    alertMonitor.addDependencies(primaryServer, secondaryServer);
    alertMonitor.addDependencies(showAlerts, speakAlerts, speechEngine);
  }

  public final static Control[] inCreationOrder = Control.getControlsInCreationOrder();
  public final static Control[] inRestoreOrder = Control.getControlsInRestoreOrder();
  private static boolean restored = false;

  public static void restore () {
    synchronized (inRestoreOrder) {
      if (!restored) {
        restored = true;
        Control.restoreCurrentValues(inRestoreOrder);
        AlertNotification.create();
      }
    }
  }
}
