package org.nbp.common;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

import java.io.IOException;
import java.io.File;

import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;

import android.util.Log;
import android.os.Environment;

public abstract class FileSystems {
  private final static String LOG_TAG = FileSystems.class.getName();

  private final static Map<String, File> fileSystems = new LinkedHashMap<String, File>();

  private final static boolean addFileSystem (
    String label, File mountpoint, boolean isRemovable
  ) {
    if (!isRemovable) {
      if (!mountpoint.canRead()) return false;
      if (!mountpoint.canExecute()) return false;
    }

    fileSystems.put(
      String.format("%s [%s]", label, mountpoint.getAbsolutePath()),
      mountpoint
    );

    return true;
  }

  private final static boolean addFileSystem (
    String label, String mountpoint, boolean isRemovable
  ) {
    return addFileSystem(label, new File(mountpoint), isRemovable);
  }

  private final static void addRemovableFileSystems () {
    try {
      Reader reader = new FileReader("/system/etc/vold.fstab");

      try {
        BufferedReader buffer = new BufferedReader(reader);

        try {
          while (true) {
            String line = buffer.readLine();
            if (line == null) break;

            String[] fields = line.split("\\s+");
            if (fields.length < 3) continue;
            if (!fields[0].equals("dev_mount")) continue;

            String label = fields[1];
            String mountpoint = fields[2];
            addFileSystem(label, mountpoint, true);
          }
        } finally {
          buffer.close();
        }
      } finally {
        reader.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, exception.getMessage());
    }
  }

  static {
    if (!Environment.isExternalStorageRemovable()) {
      addFileSystem("internal", Environment.getExternalStorageDirectory(), false);
    }

    addRemovableFileSystems();

    addFileSystem("system", Environment.getRootDirectory(), false);
    addFileSystem("data", Environment.getDataDirectory(), false);
    addFileSystem("cache", Environment.getDownloadCacheDirectory(), false);

    addFileSystem("root", "/", false);
  }

  private FileSystems () {
  }

  public static Set<String> getLabels () {
    return fileSystems.keySet();
  }

  public static File getMountpoint (String label) {
    return fileSystems.get(label);
  }
}
