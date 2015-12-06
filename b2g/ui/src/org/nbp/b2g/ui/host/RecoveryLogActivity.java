package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class RecoveryLogActivity extends CommandViewerActivity {
  private final static String LOG_TAG = RecoveryLogActivity.class.getName();

  @Override
  protected final String[] getCommand () {
    String[] command = new String[] {
      "su", "-c", "cat /cache/recovery/last_log"
    };

    return command;
  }
}
