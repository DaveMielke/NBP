package org.nbp.editor;

import java.io.File;

import android.net.Uri;
import static android.content.ContentResolver.SCHEME_FILE;

public abstract class ApplicationUtilities {
  public static File getFile (Uri uri) {
    String scheme = uri.getScheme();

    if (SCHEME_FILE.equals(scheme)) {
      return new File(uri.getPath());
    }

    return null;
  }

  private ApplicationUtilities () {
  }
}
