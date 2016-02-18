package org.nbp.editor;

import java.io.File;

import org.nbp.common.CommonActivity;
import org.nbp.common.FileFinder;
import org.nbp.common.FileSystems;

import org.nbp.common.OutgoingMessage;
import android.net.Uri;

import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.AsyncTask;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.SpannableStringBuilder;

import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class EditorActivity extends CommonActivity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  private File filesDirectory;

  private SharedPreferences prefs;
  private final static String PREF_CHECKPOINT_NAME = "checkpoint-name";
  private final static String PREF_CHECKPOINT_PATH = "checkpoint-path";

  private EditText editArea = null;
  private TextView currentPath = null;
  private File currentFile = null;
  private boolean hasChanged = false;

  protected final Activity getActivity () {
    return this;
  }

  private final void showActivityResultCode (int code) {
  }

  private void setCurrentFile (File file) {
    synchronized (this) {
      String path;

      if (file != null) {
        path = file.getAbsolutePath();
      } else {
        path = getString(R.string.hint_new_file);
      }

      currentFile = file;
      currentPath.setText(path);
    }
  }

  private void setCurrentFile (File file, CharSequence content) {
    synchronized (this) {
      setCurrentFile(file);
      editArea.setText(content);
      hasChanged = false;
    }
  }

  private void setCurrentFile () {
    setCurrentFile(null, "");
  }

  private final void saveFile (File file, final Runnable onSaved) {
    CharSequence content;

    synchronized (this) {
      if (file == null) file = currentFile;
      content = editArea.getText();
      hasChanged = false;
    }

    final File f = file;
    final CharSequence c = content;

    new AsyncTask<Void, Void, Void>() {
      AlertDialog dialog;

      @Override
      protected void onPreExecute () {
        dialog = new AlertDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setTitle(R.string.alert_writing_file)
                                .setMessage(f.getAbsolutePath())
                                .create();

        dialog.show();
      }

      @Override
      public Void doInBackground (Void... arguments) {
        FileHandler.writeFile(f, c);
        return null;
      }

      @Override
      public void onPostExecute (Void result) {
        dialog.dismiss();

        showMessage(
          String.format(
            "%s: %s",
            getString(R.string.alert_file_saved),
            f.getAbsolutePath()
          ),

          onSaved
        );
      }
    }.execute();
  }

  private final void saveFile (Runnable onSaved) {
    saveFile(null, onSaved);
  }

  private final void saveFile (File file) {
    saveFile(file, null);
  }

  private final void saveFile () {
    saveFile(null, null);
  }

  private final void testHasChanged (final Runnable onSaved) {
    if (hasChanged) {
      OnDialogClickListener positiveListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          if (currentFile != null) {
            saveFile(onSaved);
          } else {
            findFile(true,
              new FileFinder.FileHandler() {
                @Override
                public void handleFile (File file) {
                  if (file != null) saveFile(file, onSaved);
                }
              }
            );
          }
        }
      };

      OnDialogClickListener negativeListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          onSaved.run();
        }
      };

      new AlertDialog.Builder(this)
                     .setTitle(R.string.changed_title)
                     .setMessage(R.string.changed_message)
                     .setPositiveButton(R.string.changed_button_positive, positiveListener)
                     .setNeutralButton(R.string.changed_button_neutral, null)
                     .setNegativeButton(R.string.changed_button_negative, negativeListener)
                     .show();
    } else {
      onSaved.run();
    }
  }

  private final void editFile (final File file, final String path) {
    new AsyncTask<Void, Void, CharSequence>() {
      AlertDialog dialog;

      @Override
      protected void onPreExecute () {
        dialog = new AlertDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setTitle(R.string.alert_reading_file)
                                .setMessage(file.getAbsolutePath())
                                .create();

        dialog.show();
      }

      @Override
      protected CharSequence doInBackground (Void... arguments) {
        final SpannableStringBuilder sb = new SpannableStringBuilder();
        FileHandler.readFile(file, sb);
        return sb.subSequence(0, sb.length());
      }

      @Override
      protected void onPostExecute (CharSequence content) {
        dialog.dismiss();

        setCurrentFile(
          (path != null)? new File(path): file,
          content
        );
      }
    }.execute();
  }

  private final void editFile (final File file) {
    editFile(file, null);
  }

  private final File getDocumentsDirectory () {
    File directory = Environment.getExternalStoragePublicDirectory("Documents");

    if (!directory.exists()) {
      if (!directory.mkdir()) {
        Log.w(LOG_TAG, ("unable to create documents directory: " + directory.getAbsolutePath()));
        return null;
      }
    }

    return directory;
  }

  private final void addRootLocation (FileFinder.Builder builder, String name, File location) {
    builder.addRootLocation(FileSystems.makeLabel(name, location), location);
  }

  private final void findFile (boolean forWriting, FileFinder.FileHandler handler) {
    FileFinder.Builder builder = new FileFinder
      .Builder(this)
      .setForWriting(forWriting)
      ;

    if (currentFile != null) {
      addRootLocation(builder, "current", currentFile.getParentFile());
    }

    {
      File directory = getDocumentsDirectory();

      if (directory != null) {
        addRootLocation(builder, "documents", directory);
      }
    }

    for (String label : FileSystems.getRemovableLabels()) {
      builder.addRootLocation(label);
    }

    builder.findFile(handler);
  }

  private void menuAction_new () {
    testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          setCurrentFile();
        }
      }
    );
  }

  private void menuAction_open () {
    testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          findFile(false,
            new FileFinder.FileHandler() {
              @Override
              public void handleFile (File file) {
                if (file != null) editFile(file);
              }
            }
          );
        }
      }
    );
  }

  private void menuAction_save () {
    if (currentFile == null) {
      menuAction_saveAs();
    } else {
      saveFile();
    }
  }

  private void menuAction_saveAs () {
    findFile(true,
      new FileFinder.FileHandler() {
        @Override
        public void handleFile (final File file) {
          if (file != null) {
            saveFile(file,
              new Runnable() {
                @Override
                public void run () {
                  setCurrentFile(file);
                }
              }
            );
          }
        }
      }
    );
  }

  private void menuAction_send () {
    File file = currentFile;

    if (currentFile != null) {
      OutgoingMessage message = new OutgoingMessage();
      message.addAttachment(currentFile);

      if (message.getAttachments().length > 0) {
        if (message.send()) {
        }
      }
    } else {
      showMessage(R.string.alert_unsendable_new_file);
    }
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case R.id.options_new:
        menuAction_new();
        return true;

      case R.id.options_open:
        menuAction_open();
        return true;

      case R.id.options_save:
        menuAction_save();
        return true;

      case R.id.options_saveAs:
        menuAction_saveAs();
        return true;

      case R.id.options_send:
        menuAction_send();
        return true;

      default:
        return false;
    }
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  private final void prepareMenuButton () {
    if (getActionBar() == null) {
      Button button = (Button)findViewById(R.id.menu_button);
      button.setVisibility(button.VISIBLE);

      button.setOnClickListener(
        new Button.OnClickListener() {
          @Override
          public void onClick (View view) {
            editArea.requestFocus();
            getActivity().openOptionsMenu();
          }
        }
      );
    }
  }

  private final void checkpointFile () {
    synchronized (this) {
      String oldName = prefs.getString(PREF_CHECKPOINT_NAME, null);
      String newName;

      if (oldName == null) {
        newName = "checkpoint1";
      } else {
        StringBuilder sb = new StringBuilder(oldName);
        int last = oldName.length() - 1;

        sb.setCharAt(last, (oldName.charAt(last) == '1')? '2': '1');
        newName = sb.toString();
      }

      final File oldFile = (oldName != null)? new File(filesDirectory, oldName): null;
      final File newFile = new File(filesDirectory, newName);
      final String path = (currentFile != null)? currentFile.getAbsolutePath(): "";

      saveFile(newFile,
        new Runnable() {
          @Override
          public void run () {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PREF_CHECKPOINT_NAME, newFile.getName());
            editor.putString(PREF_CHECKPOINT_PATH, path);

            if (editor.commit()) {
              if (oldFile != null) {
                oldFile.delete();
              }
            }
          }
        }
      );
    }
  }

  private final void restoreFile () {
    synchronized (this) {
      String name = prefs.getString(PREF_CHECKPOINT_NAME, null);

      if (name != null) {
        String path = prefs.getString(PREF_CHECKPOINT_PATH, null);

        if (path != null) {
          File file = new File(filesDirectory, name);
          editFile(file, path);
        }
      }
    }
  }

  private final void addInputFilters () {
    InputFilter hasChangedMonitor = new InputFilter() {
      @Override
      public CharSequence filter (
        CharSequence src, int srcStart, int srcEnd,
        Spanned dst, int dstStart, int dstEnd
      ) {
        hasChanged = true;
        return null;
      }
    };

    InputFilter[] inputFilters = new InputFilter[] {
      hasChangedMonitor
    };

    editArea.setFilters(inputFilters);
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ApplicationContext.setMainActivity(this);

    filesDirectory = getFilesDir();
    prefs = getSharedPreferences("editor", MODE_PRIVATE);

    setContentView(R.layout.editor);
    currentPath = (TextView)findViewById(R.id.current_file);
    editArea = (EditText)findViewById(R.id.edit_area);
    setCurrentFile();

    showReportedErrors();
    prepareMenuButton();
    addInputFilters();
    restoreFile();
  }

  @Override
  protected void onResume () {
    super.onResume();

    editArea.requestFocus();
  }

  @Override
  protected void onPause () {
    super.onPause();
    checkpointFile();
  }

  @Override
  protected void onDestroy () {
    try {
    } finally {
      super.onDestroy();
    }
  }
}
