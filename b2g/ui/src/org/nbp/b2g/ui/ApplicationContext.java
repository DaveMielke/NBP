package org.nbp.b2g.ui;

import android.content.Context;
import org.nbp.common.CommonContext;
import org.nbp.b2g.ui.host.ScreenMonitor;
import org.liblouis.Louis;

public abstract class ApplicationContext extends CommonContext {
  public static boolean setContext (Context context) {
    if (!CommonContext.setContext(context)) return false;

    Louis.setLogLevel(ApplicationParameters.LIBLOUIS_LOG_LEVEL);
    Louis.initialize(context);

    HostMonitor.monitorEvents(context);
    Clipboard.setClipboard(context);

    Controls.restoreCurrentValues();
    Controls.restoreSaneValues();

    Devices.speech.get().say(null);
    EventMonitors.startEventMonitors();
    enableAccessibilityService(ScreenMonitor.class);
    return true;
  }

  private ApplicationContext () {
  }
}
