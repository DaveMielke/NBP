package org.nbp.common;

import java.util.Collections;
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

  private final static void addFileSystem (String label, File mountpoint) {
    fileSystems.values().removeAll(Collections.singleton(mountpoint));

    fileSystems.put(
      String.format("%s [%s]", label, mountpoint.getAbsolutePath()),
      mountpoint
    );
  }

  private final static void addFileSystem (String label, String mountpoint) {
    addFileSystem(label, new File(mountpoint));
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
            addFileSystem(label, mountpoint);
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
    addFileSystem("internal", Environment.getExternalStorageDirectory());
    addRemovableFileSystems();
    addFileSystem("root", "/");
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
