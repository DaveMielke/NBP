package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import android.view.MenuItem;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;
import android.app.Dialog;

import org.nbp.common.FileFinder;
import java.io.File;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

public class Recent extends EditorAction {
  public Recent () {
    super();
  }

  @Override
  public void performAction (final EditorActivity editor, MenuItem item) {
    DialogFinisher finisher = new DialogFinisher() {
      @Override
      public void finishDialog (DialogHelper helper) {
        final Dialog dialog = helper.getDialog();
        ViewGroup table = (ViewGroup)helper.findViewById(R.id.recent_table);
        int uriIndex = editor.recentURIs.size();

        while (uriIndex > 0) {
          final String uri = editor.recentURIs.get(--uriIndex);
          final View row;

          if (uri.charAt(0) == File.separatorChar) {
            final File file = new File(uri);
            final File parent = file.getParentFile();

            View name = editor.newButton(
              file.getName(),
              new Button.OnClickListener() {
                @Override
                public void onClick (View vie) {
                  dialog.dismiss();
                  editor.loadContent(file);
                }
              }
            );

            View folder = editor.newButton(
              parent.getAbsolutePath(),
              new Button.OnClickListener() {
                @Override
                public void onClick (View vie) {
                  dialog.dismiss();
                  FileFinder.Builder builder = editor.newFileFinderBuilder(false);
                  builder.addRootLocation(parent);

                  builder.find(
                    new FileFinder.FileHandler() {
                      @Override
                      public void handleFile (File file) {
                        if (file != null) editor.loadContent(file);
                      }
                    }
                  );
                }
              }
            );

            row = editor.newTableRow(name, folder);
          } else {
            row = editor.newButton(uri,
              new Button.OnClickListener() {
                @Override
                public void onClick (View view) {
                  dialog.dismiss();
                  editor.loadContent(new ContentHandle(uri));
                }
              }
            );
          }

          table.addView(row);
        }
      }
    };

    editor.showDialog(R.string.menu_file_Recent, R.layout.recent_list, finisher, false);
  }
}
