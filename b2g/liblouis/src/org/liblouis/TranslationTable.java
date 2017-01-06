package org.liblouis;

import java.io.File;
import java.io.FileFilter;

public class TranslationTable {
  public final static String SUBDIRECTORY = "liblouis/tables";
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

  private class TableFile {
    private final String tableName;

    private String fileName = null;
    private File fileObject = null;

    public final String getTableName () {
      return tableName;
    }

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

    private TableFile (String name) {
      tableName = name;
    }
  }

  private final TableFile forwardTable;
  private final TableFile backwardTable;

  TranslationTable (String forwardName, String backwardName) {
    forwardTable = new TableFile(forwardName);
    backwardTable = backwardName.equals(forwardName)?
                    forwardTable: new TableFile(backwardName);
  }

  TranslationTable (String name) {
    this(name, name);
  }

  public final TableFile getForwardTable () {
    return forwardTable;
  }

  public final TableFile getBackwardTable () {
    return backwardTable;
  }

  public final String getForwardTableName () {
    return getForwardTable().getTableName();
  }

  public final String getBackwardTableName () {
    return getBackwardTable().getTableName();
  }

  public final String getForwardFileName () {
    return getForwardTable().getFileName();
  }

  public final String getBackwardFileName () {
    return getBackwardTable().getFileName();
  }

  public final File getForwardFileObject () {
    return getForwardTable().getFileObject();
  }

  public final File getBackwardFileObject () {
    return getBackwardTable().getFileObject();
  }

  public final static File[] getAllTableFiles () {
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
