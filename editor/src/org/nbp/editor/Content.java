package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.nbp.common.LazyInstantiator;
import org.nbp.common.CommonContext;
import org.nbp.common.CommonUtilities;

import android.util.Log;

import android.text.SpannableStringBuilder;

public abstract class Content {
  private final static String LOG_TAG = Content.class.getName();

  private final static class ContentOperationsInstantiator extends LazyInstantiator<ContentOperations> {
    private ContentOperationsInstantiator (Class<? extends ContentOperations> type) {
      super(type);
    }
  }

  private final static Map<String, ContentOperationsInstantiator> map
             = new HashMap<String, ContentOperationsInstantiator>();

  private final static String DEFAULT_EXTENSION = "";

  private static void addExtensions (Class<? extends ContentOperations> type, String... extensions) {
    ContentOperationsInstantiator instantiator = new ContentOperationsInstantiator(type);

    for (String extension : extensions) {
      map.put(extension, instantiator);
    }
  }

  static {
    addExtensions(TextOperations.class, ".txt", DEFAULT_EXTENSION);
    addExtensions(HighlightedTextOperations.class, ".hl");
    addExtensions(ASCIIBrailleOperations.class, ".brl", ".brf");

    addExtensions(DocOperations.class, ".doc");
    addExtensions(DocMOperations.class, ".docm");
    addExtensions(DocXOperations.class, ".docx");
    addExtensions(EPubOperations.class, ".epub");
    addExtensions(HTMLOperations.class, ".html", ".htm");
    addExtensions(MHTMLOperations.class, ".mhtml", ".mht");
    addExtensions(PDFOperations.class, ".pdf");
    addExtensions(PSOperations.class, ".ps");
    addExtensions(RTFOperations.class, ".rtf");
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

  public static ContentOperations getContentOperations (File file) {
    ContentOperationsInstantiator instantiator = null;

    String extension = getExtension(file);
    if (extension != null) instantiator = map.get(extension.toLowerCase());

    if (instantiator == null) instantiator = map.get(DEFAULT_EXTENSION);
    return instantiator.get();
  }

  public static boolean readFile (File file, SpannableStringBuilder content) {
    try {
      InputStream stream = new FileInputStream(file);

      try {
        getContentOperations(file).read(stream, content);
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
        getContentOperations(file).write(stream, content);

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
