package org.nbp.common;

import java.io.File;

import android.util.Log;

import android.app.Activity;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CheckBox;

import java.text.Format;
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

  private static String makeTimeFormat () {
    boolean use24Hours = DateFormat.is24HourFormat(CommonContext.getContext());

    return new StringBuilder()
          .append("yyyy-MM-dd ")
          .append(use24Hours? "HH": "h")
          .append(":mm:ss")
          .append(use24Hours? "": "a")
          .toString();
  }

  private final void setTime (TextView view, long time, Format formatter) {
    view.setText(formatter.format(time));
  }

  private final void setModified (AlertDialog dialog, File path, Format formatter) {
    TextView view = dialog.findViewById(R.id.PathManager_modified);
    long time = path.lastModified();
    setTime(view, time, formatter);
  }

  private interface StateManager {
    public boolean getState ();
    public void setState (boolean state);
  }

  private final void showState (StateManager sm, CompoundButton button) {
    boolean state = sm.getState();
    button.setChecked(state);

    button.setText(
      state? R.string.PathManager_state_true: R.string.PathManager_state_false
    );
  }

  private final void manageState (final StateManager sm, CompoundButton button) {
    showState(sm, button);

    button.setOnCheckedChangeListener(
      new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged (CompoundButton button, boolean isChecked) {
          sm.setState(isChecked);
          showState(sm, button);

          if (sm.getState() != isChecked) {
            Tones.beep();
          }
        }
      }
    );
  }

  private final void setReadable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_readable);

    StateManager sm = new StateManager() {
      @Override
      public boolean getState () {
        return path.canRead();
      }

      @Override
      public void setState (boolean state) {
        path.setReadable(state, false);
      }
    };

    manageState(sm, checkbox);
  }

  private final void setWritable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_writable);

    StateManager sm = new StateManager() {
      @Override
      public boolean getState () {
        return path.canWrite();
      }

      @Override
      public void setState (boolean state) {
        path.setWritable(state, false);
      }
    };

    manageState(sm, checkbox);
  }

  private final void setExecutable (AlertDialog dialog, final File path) {
    final CheckBox checkbox = dialog.findViewById(R.id.PathManager_executable);

    StateManager sm = new StateManager() {
      @Override
      public boolean getState () {
        return path.canExecute();
      }

      @Override
      public void setState (boolean state) {
        path.setExecutable(state, false);
      }
    };

    manageState(sm, checkbox);
  }

  public final void managePath (File path) {
    AlertDialog dialog = makeDialog();

    setDirectory(dialog, path);
    setName(dialog, path);
    setSize(dialog, path);

    {
      Format formatter = new SimpleDateFormat(makeTimeFormat());
      setModified(dialog, path, formatter);
    }

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
