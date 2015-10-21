package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.InputStream;

public class UserGuideActivity extends ViewerActivity {
  private final static String LOG_TAG = UserGuideActivity.class.getName();

  @Override
  protected final InputStream getInputStream () {
    return openAsset("b2g_ui.txt");
  }
}
