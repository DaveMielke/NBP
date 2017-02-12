package org.nbp.editor;

import android.net.Uri;
import java.io.File;
import android.content.ContentResolver;

public class ContentHandle {
  private final Uri contentUri;
  private final String mimeType;
  private boolean isWritable;

  private final ContentOperations contentOperations;
  private final String normalizedString;
  private final File contentFile;

  public ContentHandle (Uri uri, String type, boolean writable) {
    contentUri = uri;
    mimeType = type;
    isWritable = writable;

    {
      ContentOperations operations = null;
      if (type != null) operations = Content.getContentOperations(type);
      if (operations == null) operations = Content.getContentOperations(uri);
      contentOperations = operations;
    }

    {
      String scheme = uri.getScheme();

      if (ContentResolver.SCHEME_FILE.equals(scheme)) {
        contentFile = new File(uri.getPath());
        normalizedString = contentFile.getAbsolutePath();
      } else {
        contentFile = null;
        normalizedString = uri.toString();
      }
    }
  }

  public ContentHandle (File file, String type, boolean writable) {
    this(Uri.fromFile(file), type, writable);
  }

  public ContentHandle (String uri, String type, boolean writable) {
    this(Uri.parse(uri), type, writable);
  }

  public final Uri getUri () {
    return contentUri;
  }

  public final String getType () {
    return mimeType;
  }

  public final boolean canWrite () {
    return isWritable;
  }

  public final ContentOperations getOperations () {
    return contentOperations;
  }

  public final String getUriString () {
    return contentUri.toString();
  }

  public final String getNormalizedString () {
    return normalizedString;
  }

  public final File getFile () {
    return contentFile;
  }
}
