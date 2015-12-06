package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

public class RecoveryLogActivity extends CommandViewerActivity {
  @Override
  protected final String[] getCommand () {
    String[] command = new String[] {
      "su", "-c", "cat /cache/recovery/last_log"
    };

    return command;
  }
}
