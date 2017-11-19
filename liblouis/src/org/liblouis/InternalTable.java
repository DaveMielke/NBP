package org.liblouis;

import java.io.File;
import java.io.FileFilter;

public class InternalTable {
  private final String tableName;

  public InternalTable (String name) {
    tableName = name;
  }

  public final String getTableName () {
    return tableName;
  }

  public final static String SUBDIRECTORY = Louis.toAssetsPath("tables");
  public final static String EXTENSION = ".ctb";

  public final static String makeFileName (String name) {
    return name + EXTENSION;
  }

  private final static Object STATIC_LOCK = new Object();
  private static File tablesDirectory = null;

  public static File getDirectory () {
    synchronized (STATIC_LOCK) {
      if (tablesDirectory == null) {
        tablesDirectory = new File(Louis.getDataPath(), SUBDIRECTORY);
      }
    }

    return tablesDirectory;
  }

  private String fileName = null;
  private File fileObject = null;

  public final String getFileName () {
    synchronized (this) {
      if (fileName == null) {
        fileName = makeFileName(getTableName());
      }
    }

    return fileName;
  }

  public final File getFileObject () {
    synchronized (this) {
      if (fileObject == null) {
        fileObject = new File(getDirectory(), getFileName());
      }
    }

    return fileObject;
  }

  private native short getEmphasisBit (String tablePath, String emphasisClass);
  public final short getEmphasisBit (String emphasisClass) {
    return getEmphasisBit(getFileName(), emphasisClass);
  }

  private native boolean addRule (String tablePath, String rule);
  public final boolean addRule (String rule) {
    return addRule(getFileName(), rule);
  }

  public final static File[] getAllFiles () {
    return getDirectory().listFiles(
      new FileFilter() {
        @Override
        public boolean accept (File file) {
          return file.getName().endsWith(EXTENSION);
        }
      }
    );
  }
}
