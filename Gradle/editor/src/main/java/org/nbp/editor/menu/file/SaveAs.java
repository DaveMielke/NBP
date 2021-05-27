package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import java.io.File;

import android.content.DialogInterface;

public class SaveAs extends EditorAction {
  public SaveAs (EditorActivity editor) {
    super(editor);
  }

  private final void selectFormat () {
    final Content.FormatDescriptor[] formats = Content.getFormatDescriptors();
    String[] items = new String[1 + formats.length];

    {
      int count = 0;
      items[count++] = getString(R.string.format_select_all);

      for (Content.FormatDescriptor format : formats) {
        items[count++] = format.getSelectorLabel();
      }
    }

    DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        getEditor().saveFile((index == 0)? null: formats[index-1]);
      }
    };

    getEditor().newAlertDialogBuilder(R.string.format_request_select)
      .setItems(items, itemListener)
      .setNegativeButton(R.string.action_cancel, null)
      .show();
  }

  private final void confirmFormat () {
    ContentHandle handle = getEditArea().getContentHandle();
    File file = (handle != null)? handle.getFile(): null;

    if (file == null) {
      selectFormat();
    } else {
      String extension = Content.getFileExtension(file);

      final Content.FormatDescriptor format =
        (extension != null)?
        Content.getFormatDescriptorForFileExtension(extension):
        null;

      String message =
        (format != null)? format.getSelectorLabel():
        (extension == null)? getString(R.string.format_extension_none):
        String.format("%s (%s)",
          extension,
          getString(R.string.format_extension_unrecognized)
        );

      OnDialogClickListener okListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          getEditor().saveFile(format);
        }
      };

      OnDialogClickListener changeListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          selectFormat();
        }
      };

      getEditor().newAlertDialogBuilder(R.string.format_request_confirm)
        .setMessage(message)
        .setPositiveButton(R.string.action_ok, okListener)
        .setNeutralButton(R.string.action_change, changeListener)
        .setNegativeButton(R.string.action_cancel, null)
        .show();
    }
  }

  @Override
  public void performAction (EditorActivity editor) {
    confirmFormat();
  }
}
