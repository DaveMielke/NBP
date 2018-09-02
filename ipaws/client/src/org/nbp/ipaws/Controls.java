package org.nbp.ipaws;
import org.nbp.ipaws.controls.*;

import org.nbp.common.controls.Control;

public abstract class Controls {
  private Controls () {
  }

  public final static AlertMonitorControl alertMonitor = new AlertMonitorControl();

  public final static ServerNameControl serverName = new ServerNameControl();

  public final static Control[] ALL = new Control[] {
    alertMonitor,

    serverName
  };

  public final static void restore () {
    for (Control control : ALL) {
      control.restoreCurrentValue();
    }
  }
}
