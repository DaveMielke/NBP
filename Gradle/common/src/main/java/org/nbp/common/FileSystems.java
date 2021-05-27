package org.nbp.common;

import java.util.Map;
import java.util.LinkedHashMap;

import java.util.Set;
import java.util.LinkedHashSet;

import java.io.IOException;
import java.io.File;

import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;

import android.util.Log;
import android.os.Environment;

public abstract class FileSystems {
  private final static String LOG_TAG = FileSystems.class.getName();

  private static class Entry {
    private final File mountpoint;

    public final File getMountpoint () {
      return mountpoint;
    }

    public Entry (File mountpoint) {
      this.mountpoint = mountpoint;
    }
  }

  private final static Map<String, Entry> fileSystems = new LinkedHashMap<String, Entry>();
  private final static Set<String> removableFileSystems = new LinkedHashSet<String>();

  public static String makeLabel (String name, File file) {
    return String.format("%s [%s]", name, file.getAbsolutePath());
  }

  private final static boolean addFileSystem (
    String name, File mountpoint, boolean isRemovable
  ) {
    String label = makeLabel(name, mountpoint);
    Entry fs = new Entry(mountpoint);

    if (isRemovable) {
      removableFileSystems.add(label);
    } else {
      if (!mountpoint.canRead()) return false;
      if (!mountpoint.canExecute()) return false;
    }

    fileSystems.put(label, fs);
    return true;
  }

  private final static boolean addFileSystem (
    String name, String mountpoint, boolean isRemovable
  ) {
    return addFileSystem(name, new File(mountpoint), isRemovable);
  }

  private final static void addRemovableFileSystems () {
    String voldPath = "/system/etc/vold.fstab";
    File voldFile = new File(voldPath);

    if (voldFile.exists()) {
      try {
        Reader reader = new FileReader(voldFile);

        try {
          BufferedReader buffer = new BufferedReader(reader);

          try {
            while (true) {
              String line = buffer.readLine();
              if (line == null) break;

              String[] fields = line.split("\\s+");
              if (fields.length < 3) continue;
              if (!fields[0].equals("dev_mount")) continue;

              String name = fields[1];
              String mountpoint = fields[2];
              addFileSystem(name, mountpoint, true);
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
    } else {
      String mountpointsPath = "/mnt";
      File mountpointsDirectory = new File(mountpointsPath);
      File[] mountpoints = mountpointsDirectory.listFiles();

      if (mountpoints != null) {
        for (File mountpoint : mountpoints) {
          addFileSystem(mountpoint.getName(), mountpoint.getAbsolutePath(), true);
        }
      }
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

  private static String[] toArray (Set<String> set) {
    return set.toArray(new String[set.size()]);
  }

  public static String[] getAllLabels () {
    return toArray(fileSystems.keySet());
  }

  public static String[] getRemovableLabels () {
    return toArray(removableFileSystems);
  }

  public static File getMountpoint (String label) {
    return fileSystems.get(label).getMountpoint();
  }
}
