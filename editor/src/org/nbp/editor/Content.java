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

import org.nbp.common.CommonUtilities;
import org.nbp.common.LazyInstantiator;

import android.util.Log;
import android.net.Uri;

import android.text.Spanned;
import android.text.SpannableStringBuilder;
import android.text.Editable;

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

    private FormatDescriptor (
      Class<? extends ContentOperations> instantiator, int name
    ) {
      operationsInstantiator = new OperationsInstantiator(instantiator);
      formatName = getString(name);
    }

    public final ContentOperations getOperations () {
      return operationsInstantiator.get();
    }

    public final String getFormatName () {
      return formatName;
    }

    private final List<String> fileExtensions = new ArrayList<String>();
    private final List<String> mimeTypes = new ArrayList<String>();
    private String selectorLabel = null;

    private final void addFileExtension (String extension) {
      if (fileExtensions.isEmpty()) {
        StringBuilder sb = new StringBuilder();
        sb.append(extension);

        sb.append(" [");
        sb.append(formatName);
        sb.append(']');

        selectorLabel = sb.toString();
      }

      fileExtensions.add(extension);
    }

    public final String[] getFileExtensions () {
      return fileExtensions.toArray(new String[fileExtensions.size()]);
    }

    public final String getFileExtension () {
      return fileExtensions.get(0);
    }

    private final void addMimeType (String type) {
      mimeTypes.add(type);
    }

    public final String[] getMimeTypes () {
      return mimeTypes.toArray(new String[mimeTypes.size()]);
    }

    public final String getMimeType () {
      return mimeTypes.get(0);
    }

    public final String getSelectorLabel () {
      return selectorLabel;
    }
  }

  private final static List<FormatDescriptor> formatDescriptors
            = new ArrayList<FormatDescriptor>();

  public static FormatDescriptor[] getFormatDescriptors () {
    return formatDescriptors.toArray(new FormatDescriptor[formatDescriptors.size()]);
  }

  private final static Map<String, FormatDescriptor> fileExtensionMap
             = new HashMap<String, FormatDescriptor>();

  private static void addFileExtension (FormatDescriptor descriptor, String extension) {
    descriptor.addFileExtension(extension);
    fileExtensionMap.put(extension, descriptor);
  }

  private final static Map<String, FormatDescriptor> mimeTypeMap
             = new HashMap<String, FormatDescriptor>();

  private static void addMimeType (FormatDescriptor descriptor, String type) {
    descriptor.addMimeType(type);
    mimeTypeMap.put(type, descriptor);
  }

  private static FormatDescriptor addFormat (
    Class<? extends ContentOperations> instantiator, int name
  ) {
    FormatDescriptor descriptor = new FormatDescriptor(instantiator, name);
    formatDescriptors.add(descriptor);
    return descriptor;
  }

  private final static FormatDescriptor DEFAULT_FORMAT_DESCRIPTOR;

  static {
    DEFAULT_FORMAT_DESCRIPTOR = addFormat(TextOperations.class, R.string.format_name_txt);
    addFileExtension(DEFAULT_FORMAT_DESCRIPTOR, ".txt");
    addMimeType(DEFAULT_FORMAT_DESCRIPTOR, "text/plain");

    {
      FormatDescriptor descriptor = addFormat(
        ASCIIBrailleOperations.class, R.string.format_name_brl
      );

      addFileExtension(descriptor, ".brl");
      addFileExtension(descriptor, ".brf");
    }

    {
      FormatDescriptor descriptor = addFormat(
        DocOperations.class, R.string.format_name_doc
      );

      addFileExtension(descriptor, ".doc");
      addMimeType(descriptor, "application/msword");
    }

    {
      FormatDescriptor descriptor = addFormat(
        DocMOperations.class, R.string.format_name_docm
      );

      addFileExtension(descriptor, ".docm");
    }

    {
      FormatDescriptor descriptor = addFormat(
        DocXOperations.class, R.string.format_name_docx
      );

      addFileExtension(descriptor, ".docx");
      addMimeType(descriptor, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

    {
      FormatDescriptor descriptor = addFormat(
        EPubOperations.class, R.string.format_name_epub
      );

      addFileExtension(descriptor, ".epub");
    }

    {
      FormatDescriptor descriptor = addFormat(
        HTMLOperations.class, R.string.format_name_html
      );

      addFileExtension(descriptor, ".html");
      addFileExtension(descriptor, ".htm");
      addMimeType(descriptor, "text/html");
    }

    {
      FormatDescriptor descriptor = addFormat(
        BrailleKeywordOperations.class, R.string.format_name_kwb
      );

      addFileExtension(descriptor, ".kwb");
    }

    {
      FormatDescriptor descriptor = addFormat(
        TextKeywordOperations.class, R.string.format_name_kwt
      );

      addFileExtension(descriptor, ".kwt");
      addMimeType(descriptor, "application/x-kword");
    }

    {
      FormatDescriptor descriptor = addFormat(
        MHTMLOperations.class, R.string.format_name_mhtml
      );

      addFileExtension(descriptor, ".mhtml");
      addFileExtension(descriptor, ".mht");
    }

    {
      FormatDescriptor descriptor = addFormat(
        ODTOperations.class, R.string.format_name_odt
      );

      addFileExtension(descriptor, ".odt");
      addMimeType(descriptor, "application/vnd.oasis.opendocument.text");
    }

    {
      FormatDescriptor descriptor = addFormat(
        OXPSOperations.class, R.string.format_name_oxps
      );

      addFileExtension(descriptor, ".oxps");
    }

    {
      FormatDescriptor descriptor = addFormat(
        PDFOperations.class, R.string.format_name_pdf
      );

      addFileExtension(descriptor, ".pdf");
    }

    {
      FormatDescriptor descriptor = addFormat(
        PSOperations.class, R.string.format_name_ps
      );

      addFileExtension(descriptor, ".ps");
    }

    {
      FormatDescriptor descriptor = addFormat(
        RTFOperations.class, R.string.format_name_rtf
      );

      addFileExtension(descriptor, ".rtf");
      addMimeType(descriptor, "text/rtf");
    }

    {
      FormatDescriptor descriptor = addFormat(
        XPSOperations.class, R.string.format_name_xps
      );

      addFileExtension(descriptor, ".xps");
    }
  }

  public static String getFileExtension (File file) {
    String name = file.getName();
    if (name == null) return null;

    int index = name.lastIndexOf('.');
    if (index < 1) return null;

    String extension = name.substring(index);
    if (extension.isEmpty()) return null;

    return extension;
  }

  public static FormatDescriptor getFormatDescriptorForFileExtension (String extension) {
    return fileExtensionMap.get(extension.toLowerCase());
  }

  public static FormatDescriptor getFormatDescriptorForFileExtension (File file) {
    String extension = getFileExtension(file);
    if (extension == null) return null;
    return getFormatDescriptorForFileExtension(extension);
  }

  public static FormatDescriptor getFormatDescriptorForMimeType (String type) {
    return mimeTypeMap.get(type.toLowerCase());
  }

  public static ContentOperations getContentOperations (File file) {
    FormatDescriptor descriptor = getFormatDescriptorForFileExtension(file);
    if (descriptor == null) descriptor = DEFAULT_FORMAT_DESCRIPTOR;
    return descriptor.getOperations();
  }

  public static ContentOperations getContentOperations (Uri uri) {
    return getContentOperations(new File(uri.getPath()));
  }

  public static ContentOperations getContentOperations (String type) {
    FormatDescriptor descriptor = getFormatDescriptorForMimeType(type);
    if (descriptor == null) return null;
    return descriptor.getOperations();
  }

  public static boolean readContent (ContentHandle handle, Editable content) {
    try {
      InputStream stream = ApplicationContext.getContentResolver().openInputStream(handle.getUri());

      try {
        AuthorColors.reset();
        handle.getOperations().read(stream, content);
        EditorSpan.finishSpans(content);
        return true;
      } finally {
        stream.close();
        stream = null;
      }
    } catch (IOException exception) {
      CommonUtilities.reportError(
        LOG_TAG, "input error: %s: %s",
        handle.getNormalizedString(), exception.getMessage()
      );
    }

    return false;
  }

  public static boolean writeFile (File file, CharSequence content) {
    ContentOperations operations = getContentOperations(file);

    if (content instanceof Spanned) {
      Editable copy = new SpannableStringBuilder(content);

      if (operations.canContainMarkup()) {
        Markup.restoreRevisions(copy);
      } else {
        Markup.acceptRevisions(copy);
      }

      content = copy;
    }

    String path = file.getAbsolutePath();
    String newPath = path + ".new";
    File newFile = new File(newPath);
    newFile.delete();

    try {
      OutputStream stream = new FileOutputStream(newFile);

      try {
        operations.write(stream, content);

        if (!newFile.renameTo(file)) {
          throw new IOException(String.format(
            LOG_TAG, "%s: %s -> %s",
            getString(R.string.message_rename_failed),
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
