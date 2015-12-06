package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

public abstract class AssetViewerActivity extends ViewerActivity {
  private final static String LOG_TAG = AssetViewerActivity.class.getName();

  protected abstract String getAsset ();

  @Override
  protected final InputStream getInputStream () {
    String asset = getAsset();
    if (asset == null) return null;
    Log.d(LOG_TAG, "asset: " + asset);

    try {
      return ApplicationContext.getContext().getAssets().open(asset);
    } catch (IOException exception) {
      Log.w(LOG_TAG, "asset not opened: " + exception.getMessage());
    }

    return null;
  }
}
