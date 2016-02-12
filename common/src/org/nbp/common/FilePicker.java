package org.nbp.common;

import java.util.Set;
import java.util.TreeSet;

import java.io.File;

import android.util.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.Activity;

public class FilePicker {
  private final static String LOG_TAG = FilePicker.class.getName();

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

  private final void handleFile (File file) {
  }

  private final void show (final File reference, Set<String> itemSet) {
    final boolean haveReference = reference != null;
    String dialogTitle;

    int itemCount = itemSet.size();
    if (haveReference) itemCount += 1;
    final String[] itemArray = new String[itemCount];
    itemCount = 0;

    if (haveReference) {
      itemArray[itemCount++] = getString(R.string.FilePicker_item_up);
      dialogTitle = reference.getAbsolutePath();
    } else {
      dialogTitle = getString(R.string.FilePicker_title_roots);
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
      }
    };

    final DialogInterface.OnClickListener newFolderListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
      }
    };

    final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
        handleFile(null);
      }
    };

    new AlertDialog.Builder(owningActivity)
                   .setTitle(dialogTitle)
                   .setItems(itemArray, itemListener)
                   .setPositiveButton("New File", newFileListener)
                   .setNeutralButton("New Folder", newFolderListener)
                   .setNegativeButton("Cancel", cancelListener)
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

    for (File root : File.listRoots()) {
      items.add(root.getAbsolutePath());
    }

    show(null, items);
  }

  private FilePicker (Activity owner, File reference, FileHandler handler) {
    owningActivity = owner;
    fileHandler = handler;

    if (reference != null) {
      show(reference);
    } else {
      show();
    }
  }

  public static FilePicker show (Activity owner, File reference, FileHandler handler) {
    return new FilePicker(owner, reference, handler);
  }

  public static FilePicker show (Activity owner, FileHandler handler) {
    return show(owner, null, handler);
  }
}
