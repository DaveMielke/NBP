package org.nbp.common;

import java.util.List;
import java.util.ArrayList;

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

  private static String[] toArray (List<String> list) {
    return list.toArray(new String[list.size()]);
  }

  public interface FileHandler {
    public void handleFile (File file);
  }

  private final Activity owningActivity;
  private final FileHandler fileHandler;

  private final void handleFile (File file) {
  }

  private final void show (
    final File reference, List<String> itemList, int currentItem
  ) {
    final boolean haveReference = reference != null;
    String dialogTitle = getString(R.string.FilePicker_title_roots);

    if (haveReference) {
      itemList.add(0, getString(R.string.FilePicker_item_up));
      dialogTitle = reference.getAbsolutePath();
    }

    final String[] itemArray = toArray(itemList);

    final DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int itemIndex) {
        if (haveReference && (itemIndex == 0)) {
          File parent = reference.getParentFile();

          if (parent == null) {
            show();
          } else {
            show(parent, reference.getName());
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
                   .setSingleChoiceItems(itemArray, currentItem, itemListener)
                   .setPositiveButton("New File", newFileListener)
                   .setNeutralButton("New Folder", newFolderListener)
                   .setNegativeButton("Cancel", cancelListener)
                   .setCancelable(false)
                   .show();
  }

  private final void show (File reference, String currentName) {
    if (reference.isDirectory()) {
      List<String> items = new ArrayList<String>();
      int currentIndex = -1;

      for (File file : reference.listFiles()) {
        String name = file.getName();
        char indicator = 0;

        if (currentIndex == -1) {
          if (currentName != null) {
            if (currentName.equals(name)) {
              currentIndex = items.size();
            }
          }
        }

        if (file.isDirectory()) {
          indicator = File.separatorChar;
        }

        if (indicator != 0) name += indicator;
        items.add(name);
      }

      show(reference, items, currentIndex);
    } else {
      handleFile(reference);
    }
  }

  private final void show (File reference) {
    show(reference, null);
  }

  private final void show () {
    List<String> roots = new ArrayList<String>();

    for (File root : File.listRoots()) {
      roots.add(root.getAbsolutePath());
    }

    show(null, roots, -1);
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
