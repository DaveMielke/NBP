package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import android.text.SpannableStringBuilder;

public abstract class Content {
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

  public static void readFile (File file, SpannableStringBuilder input) {
    getOperations(file).read(file, input);
  }

  public static void writeFile (File file, CharSequence output) {
    getOperations(file).write(file, output);
  }

  protected Content () {
  }
}
