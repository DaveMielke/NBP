package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import android.text.SpannableStringBuilder;

public abstract class FileHandler {
  private final static Map<String, FileHandler> map = new HashMap<String, FileHandler>();

  static {
    map.put(null, new DefaultFileHandler());

    {
      FileHandler handler = new WordFileHandler();
      map.put("doc", handler);
      map.put("docx", handler);
    }
  }

  private static String getExtension (File file) {
    String name = file.getName();

    int index = name.lastIndexOf('.');
    if (index == -1) return null;

    String extension = name.substring(index);
    if (extension.isEmpty()) return null;

    return extension;
  }

  public static FileHandler get (File file) {
    String extension = getExtension(file);

    if (extension != null) {
      FileHandler handler = map.get(extension);
      if (handler != null) return handler;
    }

    return map.get(null);
  }

  public abstract void read (File file, SpannableStringBuilder sb);
  public abstract void write (File file, SpannableStringBuilder sb);

  protected FileHandler () {
  }
}
