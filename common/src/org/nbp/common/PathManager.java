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

  private final AlertDialog makeDialog () {
    AlertDialog.Builder builder = newAlertDialogBuilder();
    setTitle(builder, R.string.PathManager_title_main, null);
    setView(builder, R.layout.path_manager);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          dialog.dismiss();
        }
      }
    );

    AlertDialog dialog = builder.create();
    dialog.show();
    return dialog;
  }

  private final void setDirectory (AlertDialog dialog, File path) {
    TextView view = dialog.findViewById(R.id.PathManager_directory);
    String directory = path.getParentFile().getAbsolutePath() + File.separator;
    view.setText(directory);
  }

  private final void setName (AlertDialog dialog, File path) {
    TextView view = dialog.findViewById(R.id.PathManager_name);
    String name = path.getName();
    view.setText(name);
  }

  private final void setIsWriteProtected (CompoundButton button, File path) {
    button.setChecked(!path.canWrite());
  }

  private final void setWritability (AlertDialog dialog, final File path) {
    CheckBox checkbox = dialog.findViewById(R.id.PathManager_write_no);
    setIsWriteProtected(checkbox, path);

    checkbox.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          path.setWritable(!isChecked, false);
          setIsWriteProtected(button, path);
        }
      }
    );
  }

  public final void managePath (File path) {
    AlertDialog dialog = makeDialog();
    setDirectory(dialog, path);
    setName(dialog, path);
    setWritability(dialog, path);
  }
}
