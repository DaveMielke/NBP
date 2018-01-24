package org.liblouis;

import java.io.File;
import java.io.FileFilter;

public class InternalTable {
  private final String tableName;

  public InternalTable (String name) {
    tableName = name;
  }

  public final String getName () {
    return tableName;
  }

  public final static String SUBDIRECTORY = Louis.toAssetsPath("tables");

  private final static Object STATIC_LOCK = new Object();
  private static File tablesDirectory = null;
  private File tableFile = null;

  public static File getDirectory () {
    synchronized (STATIC_LOCK) {
      if (tablesDirectory == null) {
        tablesDirectory = new File(Louis.getDataPath(), SUBDIRECTORY);
      }
    }

    return tablesDirectory;
  }

  public final File getFile () {
    synchronized (this) {
      if (tableFile == null) {
        tableFile = new File(getDirectory(), tableName);
      }
    }

    return tableFile;
  }

  public final String getPath () {
    return getFile().getAbsolutePath();
  }

  private native short getEmphasisBit (String tablePath, String emphasisClass);
  public final short getEmphasisBit (String emphasisClass) {
    synchronized (Louis.NATIVE_LOCK) {
      return getEmphasisBit(getPath(), emphasisClass);
    }
  }

  private native boolean addRule (String tablePath, String rule);
  public final boolean addRule (String rule) {
    synchronized (Louis.NATIVE_LOCK) {
      return addRule(getPath(), rule);
    }
  }

  public final static File[] getAllFiles () {
    final String[] extensions = new String[] {".ctb", ".utb"};

    return getDirectory().listFiles(
      new FileFilter() {
        @Override
        public boolean accept (File file) {
          String name = file.getName();

          for (String extension : extensions) {
            if (name.endsWith(extension)) return true;
          }

          return false;
        }
      }
    );
  }
}
