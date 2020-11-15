package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public abstract class ShortcutAction extends EditorAction {
  public ShortcutAction (EditorActivity editor) {
    super(editor);
  }

  public final void performShortcutAction (EditorActivity editor, String action) {
    ContentHandle handle = getEditArea().getContentHandle();

    if (handle != null) {
      File file = handle.getFile();

      if (file != null) {
        Intent launchIntent = new Intent(editor, EditorActivity.class);
        launchIntent.setAction(Intent.ACTION_EDIT);
        launchIntent.setData(Uri.fromFile(file));

        launchIntent.addFlags(
          Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        Intent installIntent = new Intent(action);
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, file.getName());
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(editor, R.drawable.nbp_editor));
        editor.sendBroadcast(installIntent);
      } else {
        showMessage(R.string.message_not_file);
      }
    } else {
      showMessage(R.string.message_shortcut_new);
    }
  }
}
