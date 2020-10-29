package org.nbp.common;

import java.io.File;

import android.util.Log;

import android.app.Activity;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CheckBox;

import java.text.SimpleDateFormat;
import android.text.format.DateFormat;

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
    if (path.isDirectory()) name += File.separator;
    view.setText(name);
  }

  private final void setSize (AlertDialog dialog, File path) {
    TextView view = dialog.findViewById(R.id.PathManager_size);
    long bytes = path.length();
    view.setText(Long.toString(bytes));
  }

  private final void setTime (TextView view, long time) {
    String format = DateFormat.is24HourFormat(CommonContext.getContext())? "HH:mm": "h:mma";
    format = "yyyy-MM-dd " + format;
    view.setText(new SimpleDateFormat(format).format(time));
  }

  private final void setModified (AlertDialog dialog, File path) {
    TextView view = dialog.findViewById(R.id.PathManager_modified);
    long time = path.lastModified();
    setTime(view, time);
  }

  private interface PathStateManager {
    public boolean getState ();
    public void setState (boolean state);
  }

  private final void showState (CompoundButton button, PathStateManager psm) {
    boolean state = psm.getState();
    button.setChecked(state);

    button.setText(
      state? R.string.PathManager_state_true:
      R.string.PathManager_state_false
    );
  }

  private final void manageState (CheckBox checkbox, final PathStateManager psm) {
    showState(checkbox, psm);

    checkbox.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          psm.setState(isChecked);
          showState(button, psm);
          if (button.isChecked() == isChecked) Tones.beep();
        }
      }
    );
  }

  private final void setReadable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_readable);

    PathStateManager psm = new PathStateManager() {
      @Override
      public boolean getState () {
        return path.canRead();
      }

      @Override
      public void setState (boolean state) {
        path.setReadable(state, false);
      }
    };

    manageState(checkbox, psm);
  }

  private final void setWritable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_writable);

    PathStateManager psm = new PathStateManager() {
      @Override
      public boolean getState () {
        return path.canWrite();
      }

      @Override
      public void setState (boolean state) {
        path.setWritable(state, false);
      }
    };

    manageState(checkbox, psm);
  }

  private final void setExecutable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_executable);

    PathStateManager psm = new PathStateManager() {
      @Override
      public boolean getState () {
        return path.canExecute();
      }

      @Override
      public void setState (boolean state) {
        path.setExecutable(state, false);
      }
    };

    manageState(checkbox, psm);
  }

  public final void managePath (File path) {
    AlertDialog dialog = makeDialog();

    setDirectory(dialog, path);
    setName(dialog, path);

    setSize(dialog, path);
    setModified(dialog, path);

    {
      TextView view = dialog.findViewById(R.id.PathManager_executable_label);

      if (path.isDirectory()) {
        view.setText(R.string.PathManager_searchable);
      } else {
        view.setText(R.string.PathManager_executable);
      }
    }

    setReadable(dialog, path);
    setWritable(dialog, path);
    setExecutable(dialog, path);
  }
}
