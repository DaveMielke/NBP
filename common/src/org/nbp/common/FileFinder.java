package org.nbp.common;

import java.util.Set;
import java.util.TreeSet;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.File;

import android.util.Log;
import android.content.Context;

import android.app.Activity;
import android.os.AsyncTask;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class FileFinder {
  private final static String LOG_TAG = FileFinder.class.getName();

  public interface FileHandler {
    public void handleFile (File file);
  }

  private final Activity owningActivity;
  private final boolean mayCreate;
  private final FileHandler fileHandler;
  private final Map<String, File> rootTable = new LinkedHashMap<String, File>();

  private File currentReference = null;

  private final String getString (int resource) {
    return owningActivity.getString(resource);
  }

  private final View inflateLayout (int resource) {
    return owningActivity.getLayoutInflater().inflate(resource, null);
  }

  private final View findView (int id) {
    return owningActivity.findViewById(id);
  }

  private final View findView (DialogInterface dialog, int id) {
    return ((AlertDialog)dialog).findViewById(id);
  }

  private final void handleFile (File file) {
    fileHandler.handleFile(file);
  }

  private final void requestCancelled () {
    handleFile(null);
  }

  private final AlertDialog.Builder newAlertDialogBuilder () {
    return new AlertDialog.Builder(owningActivity)
                          .setCancelable(false)
                          ;
  }

  private final void showMessage (
    CharSequence message,
    DialogInterface.OnClickListener dismissListener
  ) {
    newAlertDialogBuilder()
      .setMessage(message)
      .setNeutralButton(R.string.FileFinder_action_dismiss, dismissListener)
      .show();
  }

  private final void setButtonEnabled (DialogInterface dialog, int button, boolean enabled) {
    ((AlertDialog)dialog).getButton(button).setEnabled(enabled);
  }

  private final void setDoneButton (
    AlertDialog.Builder builder,
    DialogInterface.OnClickListener listener
  ) {
    builder.setPositiveButton(R.string.FileFinder_action_done, listener);
  }

  private final void setBackButton (AlertDialog.Builder builder) {
    builder.setNeutralButton(
      R.string.FileFinder_action_back,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          showListing(currentReference);
        }
      }
    );
  }

  private final void setCancelButton (AlertDialog.Builder builder) {
    builder.setNegativeButton(
      R.string.FileFinder_action_cancel,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          requestCancelled();
        }
      }
    );
  }

  private final void setEditedPath (AlertDialog dialog, File reference) {
    String directory;
    String file;

    if (!reference.isAbsolute()) {
      directory = currentReference.getAbsolutePath();
      file = reference.getPath();
    } else if (reference.isDirectory()) {
      directory = reference.getAbsolutePath();
      file = "";
    } else {
      directory = reference.getParentFile().getAbsolutePath();
      file = reference.getName();
    }

    TextView directoryView = (TextView)dialog.findViewById(R.id.PathEditor_directory);
    directoryView.setText(directory);

    EditText fileView = (EditText)dialog.findViewById(R.id.PathEditor_file);
    fileView.setText(file);
    int length = fileView.length();

    if (length > 0) {
      fileView.setSelection(length);
    } else {
      fileView.setText(R.string.FileFinder_hint_file_name);
      fileView.setSelection(0, fileView.length());
    }
  }

  private final File getEditedPath (DialogInterface dialog) {
    TextView directoryView = (TextView)findView(dialog, R.id.PathEditor_directory);
    String directory = directoryView.getText().toString();
    if (directory.isEmpty()) return null;

    EditText fileView = (EditText)findView(dialog, R.id.PathEditor_file);
    String file = fileView.getText().toString().trim();
    if (file.isEmpty()) return null;

    return new File(new File(directory), file);
  }

  private final void showPathProblem (final File file, int problem) {
    showMessage(
      String.format(
        "%s: %s",
        getString(problem),
        file.getAbsolutePath()
      ),

      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          showPathEditor(file);
        }
      }
    );
  }

  private final void showPathEditor (File reference) {
    AlertDialog.Builder builder = newAlertDialogBuilder()
      .setTitle(R.string.FileFinder_action_path)
      .setView(inflateLayout(R.layout.path_editor));

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          File file = getEditedPath(dialog);

          if (file == null) {
            requestCancelled();
          } else {
            if (!file.isAbsolute()) {
              file = new File(currentReference, file.getPath());
            }

            if (!file.exists()) {
              File directory = file.getParentFile();

              if (directory.isDirectory() || directory.mkdirs()) {
                handleFile(file);
              } else {
                showPathProblem(directory, R.string.FileFinder_message_uncreatable_directory);
              }
            } else if (file.isFile()) {
              handleFile(file);
            } else if (file.isDirectory()) {
              showListing(file);
            } else {
              showPathProblem(file, R.string.FileFinder_message_uneditable_file);
            }
          }
        }
      }
    );

    setBackButton(builder);
    setCancelButton(builder);

    AlertDialog dialog = builder.create();
    dialog.show();
    setEditedPath(dialog, reference);
  }

  private interface ListingCreator {
    public Set<String> createListing ();
  }

  private final void showListing (final File reference, final ListingCreator listingCreator) {
    currentReference = reference;
    final boolean haveReference = reference != null;

    new AsyncTask<Void, Void, AlertDialog.Builder>() {
      AlertDialog message = newAlertDialogBuilder()
        .setMessage(R.string.FileFinder_message_creating_listing)
        .create();

      @Override
      protected void onPreExecute () {
        message.show();
      }

      @Override
      protected AlertDialog.Builder doInBackground (Void... arguments) {
        AlertDialog.Builder builder = newAlertDialogBuilder();

        if (haveReference) {
          builder.setTitle(reference.getAbsolutePath());
        } else {
          builder.setTitle(getString(R.string.FileFinder_title_main));
        }

        Set<String> listing = listingCreator.createListing();
        int count = listing.size();

        if (count == 0) {
          builder.setMessage(R.string.FileFinder_message_empty_directory);
        } else {
          final String[] items = new String[count];
          count = 0;

          for (String item : listing) {
            items[count++] = item;
          }

          builder.setItems(items,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int index) {
                String item = items[index];

                if (!haveReference) {
                  showListing(rootTable.get(item));
                } else if (item.charAt(0) == File.separatorChar) {
                  showListing(new File(item));
                } else {
                  showListing(new File(reference, item));
                }
              }
            }
          );
        }

        if (mayCreate) {
          builder.setPositiveButton(
            R.string.FileFinder_action_path,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int button) {
                showPathEditor(reference);
              }
            }
          );
        }

        builder.setNeutralButton(
          R.string.FileFinder_action_up,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int button) {
              showListing(reference.getParentFile());
            }
          }
        );

        setCancelButton(builder);
        return builder;
      }

      @Override
      protected void onPostExecute (AlertDialog.Builder builder) {
        message.dismiss();

        AlertDialog dialog = builder.create();
        dialog.show();

        if (mayCreate) {
          boolean canCreate =  haveReference
                            && (reference.isDirectory())
                            && (reference.canWrite())
                            ;

          if (!canCreate) {
            setButtonEnabled(dialog, dialog.BUTTON_POSITIVE, false);
          }
        }

        if (!haveReference) {
          setButtonEnabled(dialog, dialog.BUTTON_NEUTRAL, false);
        }
      }
    }.execute();
  }

  private final Set<String> createRootListing () {
    return rootTable.keySet();
  }

  private final Set<String> createDirectoryListing (File directory) {
    Set<String> listing = new TreeSet();

    for (File file : directory.listFiles()) {
      if (file.isHidden()) continue;

      String name = file.getName();
      char indicator = 0;

      if (file.isDirectory()) {
        if (!file.canRead()) continue;
        if (!file.canExecute()) continue;
        indicator = File.separatorChar;
      } else {
        if (!file.isFile()) continue;

        if (mayCreate) {
          if (!file.canWrite()) continue;
        } else {
          if (!file.canRead()) continue;
        }
      }

      if (indicator != 0) name += indicator;
      listing.add(name);
    }

    return listing;
  }

  private final void showListing (final File reference) {
    if (reference == null) {
      showListing(null,
        new ListingCreator() {
          @Override
          public Set<String> createListing () {
            return createRootListing();
          }
        }
      );
    } else if (reference.isDirectory()) {
      showListing(reference,
        new ListingCreator() {
          @Override
          public Set<String> createListing () {
            return createDirectoryListing(reference);
          }
        }
      );
    } else {
      handleFile(reference);
    }
  }

  private final void addRoot (int label, String path) {
    rootTable.put(
      String.format("%s [%s]", getString(label), path),
      new File(path)
    );
  }

  private FileFinder (Activity owner, File reference, boolean create, FileHandler handler) {
    owningActivity = owner;
    mayCreate = create;
    fileHandler = handler;

    addRoot(R.string.FileFinder_root_user, "/storage");
    addRoot(R.string.FileFinder_root_system, "/");

    showListing(reference);
  }

  public static FileFinder findFile (Activity owner, File reference, boolean create, FileHandler handler) {
    return new FileFinder(owner, reference, create, handler);
  }

  public static FileFinder findFile (Activity owner, boolean create, FileHandler handler) {
    return findFile(owner, null, create, handler);
  }
}
