package org.nbp.editor;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.nbp.common.LazyInstantiator;
import org.nbp.common.CommonUtilities;

import android.util.Log;

import android.text.SpannableStringBuilder;

public abstract class Content {
  private final static String LOG_TAG = Content.class.getName();

  private static String getString (int resource) {
    return ApplicationContext.getMainActivity().getString(resource);
  }

  private final static class OperationsInstantiator extends LazyInstantiator<ContentOperations> {
    private OperationsInstantiator (Class<? extends ContentOperations> type) {
      super(type);
    }
  }

  public final static class FormatDescriptor {
    private final OperationsInstantiator operationsInstantiator;
    private final String formatName;
    private final String[] fileExtensions;
    private final String selectorLabel;

    public final ContentOperations getOperations () {
      return operationsInstantiator.get();
    }

    public final String getFormatName () {
      return formatName;
    }

    public final String[] getFileExtensions () {
      return fileExtensions;
    }

    public final String getFileExtension () {
      return getFileExtensions()[0];
    }

    public final String getSelectorLabel () {
      return selectorLabel;
    }

    private final String makeSelectorLabel () {
      StringBuilder sb = new StringBuilder();
      sb.append(getFileExtension());

      sb.append(" [");
      sb.append(getFormatName());
      sb.append(']');

      return sb.toString();
    }

    private FormatDescriptor (
      Class<? extends ContentOperations> instantiator,
      int name, String[] extensions
    ) {
      operationsInstantiator = new OperationsInstantiator(instantiator);
      formatName = getString(name);
      fileExtensions = extensions;
      selectorLabel = makeSelectorLabel();
    }
  }

  private final static List<FormatDescriptor> formatDescriptors
            = new ArrayList<FormatDescriptor>();

  private final static Map<String, FormatDescriptor> extensionToFormatDescriptor
             = new HashMap<String, FormatDescriptor>();

  private static FormatDescriptor addFormat (
    Class<? extends ContentOperations> instantiator,
    int name, String... extensions
  ) {
    FormatDescriptor formatDescriptor = new FormatDescriptor(instantiator, name, extensions);
    formatDescriptors.add(formatDescriptor);

    for (String extension : extensions) {
      extensionToFormatDescriptor.put(extension, formatDescriptor);
    }

    return formatDescriptor;
  }

  private final static FormatDescriptor defaultFormatDescriptor;

  static {
    defaultFormatDescriptor = addFormat(TextOperations.class, R.string.format_name_txt, ".txt");
    addFormat(ASCIIBrailleOperations.class, R.string.format_name_brf, ".brl", ".brf");

    addFormat(DocOperations.class, R.string.format_name_doc, ".doc");
    addFormat(DocMOperations.class, R.string.format_name_docm, ".docm");
    addFormat(DocXOperations.class, R.string.format_name_docx, ".docx");
    addFormat(EPubOperations.class, R.string.format_name_epub, ".epub");
    addFormat(HTMLOperations.class, R.string.format_name_html, ".html", ".htm");
    addFormat(MHTMLOperations.class, R.string.format_name_mhtml, ".mhtml", ".mht");
    addFormat(ODTOperations.class, R.string.format_name_odt, ".odt");
    addFormat(OXPSOperations.class, R.string.format_name_oxps, ".oxps");
    addFormat(PDFOperations.class, R.string.format_name_pdf, ".pdf");
    addFormat(PSOperations.class, R.string.format_name_ps, ".ps");
    addFormat(RTFOperations.class, R.string.format_name_rtf, ".rtf");
    addFormat(XPSOperations.class, R.string.format_name_xps, ".xps");
  }

  public static FormatDescriptor[] getFormatDescriptors () {
    return formatDescriptors.toArray(new FormatDescriptor[formatDescriptors.size()]);
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

  public static FormatDescriptor getFormatDescriptor (File file) {
    String extension = getExtension(file);
    if (extension == null) return null;
    return extensionToFormatDescriptor.get(extension.toLowerCase());
  }

  public static ContentOperations getContentOperations (File file) {
    FormatDescriptor formatDescriptor = getFormatDescriptor(file);
    if (formatDescriptor == null) formatDescriptor = defaultFormatDescriptor;
    return formatDescriptor.getOperations();
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
            getString(R.string.alert_rename_failed),
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
