package org.nbp.common;

import java.util.Set;
import java.util.TreeSet;

import java.io.File;

import android.util.Log;
import android.content.Context;
import android.app.Activity;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class FileFinder {
  private final static String LOG_TAG = FileFinder.class.getName();

  private static Context getContext () {
    return CommonContext.getContext();
  }

  private static String getString (int resource) {
    return getContext().getString(resource);
  }

  public interface FileHandler {
    public void handleFile (File file);
  }

  private final Activity owningActivity;
  private final boolean mayCreate;
  private final FileHandler fileHandler;
  private final View pathEditorView;

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

  private final void setCancelButton (AlertDialog.Builder builder) {
    builder.setNegativeButton(
      R.string.FileFinder_action_cancel,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int itemIndex) {
          handleFile(null);
        }
      }
    );
  }

  private final void setDoneButton (
    AlertDialog.Builder builder,
    DialogInterface.OnClickListener listener
  ) {
    builder.setPositiveButton(R.string.FileFinder_action_done, listener);
  }

  private final void setEditedPath (AlertDialog dialog, File reference) {
    if (reference != null) {
      String path = reference.getAbsolutePath();
      int length = path.length();

      if (path.charAt(length-1) != File.separatorChar) {
        path += File.separatorChar;
        length += 1;
      }

      EditText view = (EditText)dialog.findViewById(R.id.edited_path);
      view.setText(path);
      view.setSelection(length);
    }
  }

  private final String getEditedPath (DialogInterface dialog) {
    EditText view = (EditText)findView(dialog, R.id.edited_path);
    return view.getText().toString();
  }

  private final void showNewFileDialog (File reference) {
    AlertDialog.Builder builder = new AlertDialog
      .Builder(owningActivity)
      .setTitle(R.string.FileFinder_action_newFile)
      .setView(pathEditorView)
      .setCancelable(false);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int itemIndex) {
          String path = getEditedPath(dialog);

          if (path.isEmpty()) {
            handleFile(null);
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
    AlertDialog.Builder builder = new AlertDialog
      .Builder(owningActivity)
      .setTitle(R.string.FileFinder_action_newFolder)
      .setView(pathEditorView)
      .setCancelable(false);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int itemIndex) {
          String path = getEditedPath(dialog);

          if (path.isEmpty()) {
            handleFile(null);
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

  private final void showItemListing (final File reference, Set<String> itemSet) {
    final boolean haveReference = reference != null;
    String dialogTitle;

    int itemCount = itemSet.size();
    if (haveReference) itemCount += 1;
    final String[] itemArray = new String[itemCount];
    itemCount = 0;

    if (haveReference) {
      itemArray[itemCount++] = getString(R.string.FileFinder_item_up);
      dialogTitle = reference.getAbsolutePath();
    } else {
      dialogTitle = getString(R.string.FileFinder_title_roots);
    }

    for (String item : itemSet) {
      itemArray[itemCount++] = item;
    }

    DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
        if (haveReference && (itemIndex == 0)) {
          showListing(reference.getParentFile());
        } else {
          String itemName = itemArray[itemIndex];

          if (itemName.charAt(0) == File.separatorChar) {
            showListing(new File(itemName));
          } else {
            showListing(new File(reference, itemName));
          }
        }

        dialog.dismiss();
      }
    };

    AlertDialog.Builder builder = new AlertDialog
      .Builder(owningActivity)
      .setTitle(dialogTitle)
      .setItems(itemArray, itemListener)
      .setCancelable(false);

    if (mayCreate) {
      builder.setPositiveButton(
        R.string.FileFinder_action_newFile,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int itemIndex) {
            showNewFileDialog(reference);
          }
        }
      );

      builder.setNeutralButton(
        R.string.FileFinder_action_newFolder,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int itemIndex) {
            showNewFolderDialog(reference);
          }
        }
      );
    }

    setCancelButton(builder);
    AlertDialog dialog = builder.create();
    dialog.show();

    if (mayCreate) {
      boolean canCreate =  (reference != null)
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

  private final Set<String> newItemSet () {
    return new TreeSet<String>();
  }

  private final void showRootListing () {
    Set<String> items = newItemSet();

    for (File file : File.listRoots()) {
      items.add(file.getAbsolutePath());
    }

    for (String item : System.getenv("SECONDARY_STORAGE").split(":")) {
      items.add(item);
    }

    items.add(System.getenv("EXTERNAL_STORAGE"));
    items.remove("");

    showItemListing(null, items);
  }

  private final void showDirectoryListing (File directory) {
    Set<String> items = newItemSet();

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
      items.add(name);
    }

    showItemListing(directory, items);
  }

  private final void showListing (File reference) {
    if (reference == null) {
      showRootListing();
    } else if (reference.isDirectory()) {
      showDirectoryListing(reference);
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
