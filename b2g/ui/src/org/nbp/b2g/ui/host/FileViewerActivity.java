package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;

public abstract class FileViewerActivity extends ViewerActivity {
  private final static String LOG_TAG = FileViewerActivity.class.getName();

  protected abstract File getFile ();

  @Override
  protected final InputStream getInputStream () {
    File file = getFile();
    if (file == null) return null;

    String path = file.getAbsolutePath();
    if (path == null) return null;
    Log.d(LOG_TAG, "file: " + path);

    try {
      return new FileInputStream(file);
    } catch (IOException exception) {
      Log.w(LOG_TAG, "file not opened: " + exception.getMessage());
    }

    return null;
  }
}
