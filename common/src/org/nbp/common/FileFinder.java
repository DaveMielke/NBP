package org.nbp.common;

import java.util.Set;
import java.util.TreeSet;

import java.io.File;

import android.util.Log;
import android.content.Context;
import android.app.Activity;
import android.os.AsyncTask;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.view.View;
import android.widget.EditText;

public class FileFinder {
  private final static String LOG_TAG = FileFinder.class.getName();

  public interface FileHandler {
    public void handleFile (File file);
  }

  private final Activity owningActivity;
  private final boolean mayCreate;
  private final FileHandler fileHandler;
  private final View pathEditorView;

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

  private final void setDoneButton (
    AlertDialog.Builder builder,
    DialogInterface.OnClickListener listener
  ) {
    builder.setPositiveButton(R.string.FileFinder_action_done, listener);
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
    EditText view = (EditText)dialog.findViewById(R.id.edited_path);

    if (reference != null) {
      String path = reference.getAbsolutePath();
      int length = path.length();

      if (path.charAt(length-1) != File.separatorChar) {
        path += File.separatorChar;
        length += 1;
      }

      view.setText(path);
      view.setSelection(length);
    } else {
      view.setText("");
    }
  }

  private final String getEditedPath (DialogInterface dialog) {
    EditText view = (EditText)findView(dialog, R.id.edited_path);
    return view.getText().toString().trim();
  }

  private final void showNewFileDialog (File reference) {
    AlertDialog.Builder builder = newAlertDialogBuilder()
      .setTitle(R.string.FileFinder_action_newFile)
      .setView(pathEditorView);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          String path = getEditedPath(dialog);

          if (path.isEmpty()) {
            requestCancelled();
          } else {
            File file = new File(path);
            handleFile(file);
          }
        }
      }
    );

    setCancelButton(builder);
    AlertDialog dialog = builder.create();
    dialog.show();
    setEditedPath(dialog, reference);
  }

  private final void showNewFolderDialog (File reference) {
    AlertDialog.Builder builder = newAlertDialogBuilder()
      .setTitle(R.string.FileFinder_action_newFolder)
      .setView(pathEditorView);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          String path = getEditedPath(dialog);

          if (path.isEmpty()) {
            requestCancelled();
          } else {
            File file = new File(path);
            file.mkdir();
            showListing(file);
          }
        }
      }
    );

    setCancelButton(builder);
    AlertDialog dialog = builder.create();
    dialog.show();
    setEditedPath(dialog, reference);
  }

  private interface ListingCreator {
    public Set<String> createListing ();
  }

  private final void showListing (final File reference, final ListingCreator listingCreator) {
    final boolean haveReference = reference != null;

    new AsyncTask<Void, Void, AlertDialog.Builder>() {
      AlertDialog message = newAlertDialogBuilder()
        .setMessage(R.string.FileFinder_message_listing)
        .create();

      @Override
      protected void onPreExecute () {
        message.show();
      }

      @Override
      protected AlertDialog.Builder doInBackground (Void... arguments) {
        String title;

        Set<String> listing = listingCreator.createListing();
        int count = listing.size();
        if (haveReference) count += 1;
        final String[] items = new String[count];
        count = 0;

        if (haveReference) {
          items[count++] = getString(R.string.FileFinder_item_up);
          title = reference.getAbsolutePath();
        } else {
          title = getString(R.string.FileFinder_title_roots);
        }

        for (String item : listing) {
          items[count++] = item;
        }

        AlertDialog.Builder builder = newAlertDialogBuilder()
          .setTitle(title)
          .setItems(items,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int index) {
                if (haveReference && (index == 0)) {
                  showListing(reference.getParentFile());
                } else {
                  String path = items[index];

                  if (path.charAt(0) == File.separatorChar) {
                    showListing(new File(path));
                  } else {
                    showListing(new File(reference, path));
                  }
                }
              }
            }
          );

        if (mayCreate) {
          builder.setPositiveButton(
            R.string.FileFinder_action_newFile,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int button) {
                showNewFileDialog(reference);
              }
            }
          );

          builder.setNeutralButton(
            R.string.FileFinder_action_newFolder,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int button) {
                showNewFolderDialog(reference);
              }
            }
          );
        }

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
            int[] buttons = new int[] {
              dialog.BUTTON_POSITIVE,
              dialog.BUTTON_NEUTRAL
            };

            for (int button : buttons) {
              dialog.getButton(button).setEnabled(false);
            }
          }
        }
      }
    }.execute();
  }

  private final Set<String> newListing () {
    return new TreeSet<String>();
  }

  private final Set<String> createRootListing () {
    Set<String> listing = newListing();

    for (File file : File.listRoots()) {
      listing.add(file.getAbsolutePath());
    }

    for (String item : System.getenv("SECONDARY_STORAGE").split(":")) {
      listing.add(item);
    }

    listing.add(System.getenv("EXTERNAL_STORAGE"));
    listing.remove("");

    return listing;
  }

  private final Set<String> createDirectoryListing (File directory) {
    Set<String> listing = newListing();

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

  private FileFinder (Activity owner, File reference, boolean create, FileHandler handler) {
    owningActivity = owner;
    mayCreate = create;
    fileHandler = handler;

    pathEditorView = inflateLayout(R.layout.path_editor);

    showListing(reference);
  }

  public static FileFinder findFile (Activity owner, File reference, boolean create, FileHandler handler) {
    return new FileFinder(owner, reference, create, handler);
  }

  public static FileFinder findFile (Activity owner, boolean create, FileHandler handler) {
    return findFile(owner, null, create, handler);
  }
}
