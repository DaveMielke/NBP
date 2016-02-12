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
  private final FileHandler fileHandler;
  private final View newFileView;

  private final void handleFile (File file) {
    fileHandler.handleFile(file);
  }

  private final DialogInterface.OnClickListener doneListener = new DialogInterface.OnClickListener() {
    @Override
    public void onClick (DialogInterface dialog, int itemIndex) {
      EditText view = (EditText)((AlertDialog)dialog).findViewById(R.id.new_file_path);
      String path = view.getText().toString();

      if (path.isEmpty()) {
        handleFile(null);
      } else {
        File file = new File(path);
        handleFile(file);
      }
    }
  };

  private final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
    @Override
    public void onClick (DialogInterface dialog, int itemIndex) {
      handleFile(null);
    }
  };

  private final void showNewFileDialog (File reference) {
    AlertDialog dialog = new AlertDialog
      .Builder(owningActivity)
      .setTitle(R.string.FileFinder_action_newFile)
      .setView(newFileView)
      .setPositiveButton(R.string.FileFinder_action_done, doneListener)
      .setNegativeButton(R.string.FileFinder_action_cancel, cancelListener)
      .setCancelable(false)
      .create();

    dialog.show();

    if (reference != null) {
      String path = reference.getAbsolutePath();
      int length = path.length();

      if (path.charAt(length-1) != File.separatorChar) {
        path += File.separatorChar;
        length += 1;
      }

      EditText view = (EditText)dialog.findViewById(R.id.new_file_path);
      view.setText(path);
      view.setSelection(length);
    }
  }

  private final void show (final File reference, Set<String> itemSet) {
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

    final DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
        if (haveReference && (itemIndex == 0)) {
          File parent = reference.getParentFile();

          if (parent == null) {
            show();
          } else {
            show(parent);
          }
        } else {
          String itemName = itemArray[itemIndex];

          if (itemName.charAt(0) == File.separatorChar) {
            show(new File(itemName));
          } else {
            show(new File(reference, itemName));
          }
        }

        dialog.dismiss();
      }
    };

    final DialogInterface.OnClickListener newFileListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
        showNewFileDialog(reference);
      }
    };

    final DialogInterface.OnClickListener newFolderListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
      }
    };

    new AlertDialog.Builder(owningActivity)
                   .setTitle(dialogTitle)
                   .setItems(itemArray, itemListener)
                   .setPositiveButton(R.string.FileFinder_action_newFile, newFileListener)
                   .setNeutralButton(R.string.FileFinder_action_newFolder, newFolderListener)
                   .setNegativeButton(R.string.FileFinder_action_cancel, cancelListener)
                   .setCancelable(false)
                   .show();
  }

  private final void show (File reference) {
    if (reference.isDirectory()) {
      Set<String> items = new TreeSet<String>();

      for (File file : reference.listFiles()) {
        if (file.isHidden()) continue;
        if (!file.canRead()) continue;

        String name = file.getName();
        char indicator = 0;

        if (file.isDirectory()) {
          indicator = File.separatorChar;
        }

        if (indicator != 0) name += indicator;
        items.add(name);
      }

      show(reference, items);
    } else {
      handleFile(reference);
    }
  }

  private final void show () {
    Set<String> items = new TreeSet<String>();

    for (File file : File.listRoots()) {
      items.add(file.getAbsolutePath());
    }

    for (String item : System.getenv("SECONDARY_STORAGE").split(":")) {
      items.add(item);
    }

    items.add(System.getenv("EXTERNAL_STORAGE"));
    items.remove("");

    show(null, items);
  }

  private final View inflateLayout (int resource) {
    return owningActivity.getLayoutInflater().inflate(resource, null);
  }

  private final View findView (int id) {
    return owningActivity.findViewById(id);
  }

  private FileFinder (Activity owner, File reference, FileHandler handler) {
    owningActivity = owner;
    fileHandler = handler;

    newFileView = inflateLayout(R.layout.new_file);

    if (reference != null) {
      show(reference);
    } else {
      show();
    }
  }

  public static FileFinder findFile (Activity owner, File reference, FileHandler handler) {
    return new FileFinder(owner, reference, handler);
  }

  public static FileFinder findFile (Activity owner, FileHandler handler) {
    return findFile(owner, null, handler);
  }
}
