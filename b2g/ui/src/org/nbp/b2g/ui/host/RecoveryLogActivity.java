package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.InputStream;

public class RecoveryLogActivity extends ViewerActivity {
  private final static String LOG_TAG = RecoveryLogActivity.class.getName();

  @Override
  protected final InputStream getInputStream () {
    return openFile("/cache/recovery/last_log");
  }
}
