package org.nbp.editor;

import org.nbp.common.MimeTypes;

import android.net.Uri;
import java.io.File;
import static android.content.ContentResolver.SCHEME_FILE;
import static android.content.ContentResolver.SCHEME_CONTENT;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns;

public class ContentHandle {
  private final Uri contentUri;
  private final String mimeType;
  private boolean isWritable;

  private final ContentOperations contentOperations;
  private final String normalizedString;
  private final File contentFile;

  private final String providedType;
  private final String providedName;
  private final long providedSize;

  public ContentHandle (Uri uri, String type, boolean writable) {
    contentUri = uri;
    isWritable = writable;

    {
      String scheme = uri.getScheme();

      if (SCHEME_FILE.equals(scheme)) {
        contentFile = new File(uri.getPath());
        normalizedString = contentFile.getAbsolutePath();

        providedName = contentFile.getName();
        providedSize = contentFile.length();
        providedType = MimeTypes.getMimeType(providedName);
      } else {
        contentFile = null;

        if (SCHEME_CONTENT.equals(scheme)) {
          ContentResolver resolver = ApplicationContext.getContentResolver();
          providedType = resolver.getType(uri);

          Cursor cursor = resolver.query(uri, null, null, null, null);
          cursor.moveToFirst();

          int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
          providedName = cursor.getString(nameIndex);
          normalizedString = providedName;

          int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
          providedSize = cursor.getLong(sizeIndex);

          cursor.close();
        } else {
          normalizedString = uri.toString();
          providedType = null;
          providedName = null;
          providedSize = 0;
        }
      }
    }

    if (type == null) type = providedType;
    mimeType = type;

    {
      ContentOperations operations = null;
      if (mimeType != null) operations = Content.getContentOperations(mimeType);
      if (operations == null) operations = Content.getContentOperations(uri);
      contentOperations = operations;
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

  public final String getProvidedType () {
    return providedType;
  }

  public final String getProvidedName () {
    return providedName;
  }

  public final long getProvidedSize () {
    return providedSize;
  }
}
