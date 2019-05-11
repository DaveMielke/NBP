package org.nbp.common;

import java.io.File;

import android.util.Log;

import android.app.Activity;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CheckBox;

public class PathManager extends ActivityComponent {
  private final static String LOG_TAG = PathManager.class.getName();

  public PathManager (Activity owner) {
    super(owner);
  }

  private final void setIsWriteProtected (File path, CompoundButton button) {
    button.setChecked(!path.canWrite());
  }

  public final void managePath (final File path) {
    AlertDialog.Builder builder = newAlertDialogBuilder();
    setView(builder, R.layout.path_manager);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          dialog.dismiss();
        }
      }
    );

    setTitle(builder, R.string.PathManager_title_main, null);
    AlertDialog dialog = builder.create();
    dialog.show();

    {
      String directory = path.getParentFile().getAbsolutePath() + File.separator;
      TextView view = dialog.findViewById(R.id.PathManager_directory);
      view.setText(directory);
    }

    {
      String name = path.getName();
      TextView view = dialog.findViewById(R.id.PathManager_name);
      view.setText(name);
    }

    {
      CheckBox checkbox = dialog.findViewById(R.id.PathManager_write_no);
      setIsWriteProtected(path, checkbox);

      checkbox.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged (CompoundButton button, boolean isChecked) {
            path.setWritable(!isChecked, false);
            setIsWriteProtected(path, button);
          }
        }
      );
    }
  }
}
