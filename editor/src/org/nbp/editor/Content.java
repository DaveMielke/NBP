package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;

import android.util.Log;

import android.text.SpannableStringBuilder;

public abstract class Content {
  private final static String LOG_TAG = Content.class.getName();

  private final static Map<String, ContentOperations> map = new HashMap<String, ContentOperations>();
  private final static String DEFAULT_EXTENSION = "";

  private static void addExtensions (ContentOperations operations, String... extensions) {
    for (String extension : extensions) {
      map.put(extension, operations);
    }
  }

  static {
    addExtensions(new TextOperations(), ".txt", DEFAULT_EXTENSION);
    addExtensions(new HighlightedTextOperations(), ".hl");
    addExtensions(new BRFOperations(), ".brf");
    addExtensions(new AsposeOperations(), ".doc", ".docx");
  }

  public static String getExtension (File file) {
    String name = file.getName();
    if (name == null) return null;

    int index = name.lastIndexOf('.');
    if (index < 1) return null;

    String extension = name.substring(index);
    if (extension.isEmpty()) return null;

    return extension;
  }

  public static ContentOperations getOperations (File file) {
    String extension = getExtension(file);

    if (extension != null) {
      ContentOperations operations = map.get(extension.toLowerCase());
      if (operations != null) return operations;
    }

    return map.get(DEFAULT_EXTENSION);
  }

  public static boolean readFile (File file, SpannableStringBuilder content) {
    try {
      InputStream stream = new FileInputStream(file);

      try {
        getOperations(file).read(stream, content);
        return true;
      } finally {
        stream.close();
      }
    } catch (IOException exception) {
      CommonUtilities.reportError(
        LOG_TAG, "input file error: %s: %s",
        file.getAbsolutePath(), exception.getMessage()
      );
    }

    return false;
  }

  public static boolean writeFile (File file, CharSequence content) {
    String path = file.getAbsolutePath();
    String newPath = path + ".new";
    File newFile = new File(newPath);
    newFile.delete();

    try {
      OutputStream stream = new FileOutputStream(newFile);

      try {
        getOperations(file).write(stream, content);

        if (!newFile.renameTo(file)) {
          throw new IOException(String.format(
            LOG_TAG, "%s: %s -> %s",
            CommonContext.getString(R.string.alert_rename_failed),
            newFile.getAbsolutePath(), file.getAbsolutePath()
          ));
        }

        return true;
      } finally {
        stream.close();
      }
    } catch (IOException exception) {
      CommonUtilities.reportError(
        LOG_TAG, "output file error: %s: %s",
        file.getAbsolutePath(), exception.getMessage()
      );
    }

    return false;
  }

  protected Content () {
  }
}
