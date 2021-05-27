package org.nbp.common;

import android.net.Uri;
import android.webkit.MimeTypeMap;

public abstract class MimeTypes {
  public static String getMimeType (String url) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
    if (extension == null) return null;
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
  }

  public static String getMimeType (Uri uri) {
    return getMimeType(uri.toString());
  }

  private MimeTypes () {
  }
}
