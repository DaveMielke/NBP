package org.nbp.editor;

import java.io.File;

import org.nbp.common.CommonActivity;

import android.util.Log;

import android.content.Context;
import android.content.Intent;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import android.widget.EditText;
import android.text.InputFilter;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class EditorActivity extends CommonActivity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  private EditText editArea = null;
  private TextView currentPath = null;
  private File currentFile = null;
  private boolean hasChanged = false;

  protected final Activity getActivity () {
    return this;
  }

  private final void showActivityResultCode (int code) {
  }

  private void setCurrentFile (File file, CharSequence content) {
    String path;

    if (file != null) {
      path = file.getAbsolutePath();
    } else {
      path = getString(R.string.message_new_file);
    }

    synchronized (this) {
      currentFile = file;
      currentPath.setText(path);
      editArea.setText(content);
      hasChanged = false;
    }
  }

  private void setCurrentFile () {
    setCurrentFile(null, "");
  }

  private final void saveFile () {
    File file;
    CharSequence content;

    synchronized (this) {
      file = currentFile;
      content = editArea.getText();
      hasChanged = false;
    }

    FileHandler.get(file).write(file, content);
  }

  private final void saveFile (final Runnable next) {
    new AsyncTask<Void, Void,Void>() {
      @Override
      public Void doInBackground (Void... arguments) {
        saveFile();
        return null;
      }

      @Override
      public void onPostExecute (Void result) {
        if (next != null) next.run();
      }
    }.execute();
  }

  private final void testHasChanged (final Runnable next) {
    if (hasChanged) {
      OnDialogClickListener positiveListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          saveFile(next);
        }
      };

      OnDialogClickListener negativeListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          next.run();
        }
      };

      OnDialogClickListener neutralListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
        }
      };

      new AlertDialog.Builder(this)
                     .setTitle(R.string.alert_changed_title)
                     .setMessage(R.string.alert_changed_message)
                     .setPositiveButton(R.string.alert_changed_positive, positiveListener)
                     .setNegativeButton(R.string.alert_changed_negative, negativeListener)
                     .setNeutralButton(R.string.alert_changed_neutral, neutralListener)
                     .show();
    } else {
      next.run();
    }
  }

  private final void editFile (final File file) {
    new AsyncTask<Void, Void, CharSequence>() {
      @Override
      protected CharSequence doInBackground (Void... arguments) {
        final SpannableStringBuilder sb = new SpannableStringBuilder();
        FileHandler.get(file).read(file, sb);
        return sb.subSequence(0, sb.length());
      }

      @Override
      protected void onPostExecute (CharSequence content) {
        setCurrentFile(file, content);
      }
    }.execute();
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
          findFile(
            new ActivityResultHandler() {
              @Override
              public void handleActivityResult (int code, Intent intent) {
                switch (code) {
                  case RESULT_OK:
                    editFile(new File(intent.getData().getPath()));
                    break;

                  default:
                    showActivityResultCode(code);
                    break;
                }
              }
            }
          );
        }
      }
    );
  }

  private void menuAction_save () {
    if (currentFile == null) menuAction_saveAs();
    saveFile(null);
  }

  private void menuAction_saveAs () {
  }

  private void menuAction_send () {
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

  private final void prepareActionsButton () {
    if (getActionBar() == null) {
      Button button = (Button)findViewById(R.id.actions_button);
      button.setVisibility(button.VISIBLE);

      button.setOnClickListener(
        new Button.OnClickListener() {
          @Override
          public void onClick (View view) {
            getActivity().openOptionsMenu();
          }
        }
      );
    }
  }

  private void setInputFilters () {
    editArea.setFilters(
      new InputFilter[] {
        new InputFilter() {
          @Override
          public CharSequence filter (
            CharSequence src, int srcStart, int srcEnd,
            Spanned dst, int dstStart, int dstEnd
          ) {
            while (srcStart < srcEnd) {
              char character = src.charAt(srcStart++);

              switch (character) {
                case 0X0E:
                  menuAction_new();
                  break;

                case 0X0F:
                  menuAction_open();
                  break;

                case 0X13:
                  menuAction_save();
                  break;

                default:
                  if (Character.getType(character) == Character.CONTROL) break;

                case '\f':
                case '\n':
                case '\r':
                case '\t':
                  hasChanged = true;
                  return null;
              }
            }

            return "";
          }
        }
      }
    );
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.editor);
    currentPath = (TextView)findViewById(R.id.current_file);
    editArea = (EditText)findViewById(R.id.edit_area);
    setCurrentFile();

    prepareActionsButton();
    setInputFilters();
  }

  @Override
  public void onDestroy () {
    try {
    } finally {
      super.onDestroy();
    }
  }
}
