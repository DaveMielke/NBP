package org.liblouis;

import java.io.File;
import java.io.FileFilter;

public class InternalTable {
  private final String tableList;

  public InternalTable (String tables) {
    tableList = tables;
  }

  public final String getList () {
    return tableList;
  }

  public final static String TABLE_LIST_DELIMITER = ",";

  public final String[] getNames () {
    return getList().split(TABLE_LIST_DELIMITER);
  }

  public final static String SUBDIRECTORY = Louis.toAssetsPath("tables");

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

  public static File getFile (String name) {
    return new File(getDirectory(), name);
  }

  private native static short getEmphasisBit (String tableList, String emphasisClass);
  public final short getEmphasisBit (String emphasisClass) {
    synchronized (Louis.NATIVE_LOCK) {
      return getEmphasisBit(tableList, emphasisClass);
    }
  }

  private native static boolean addRule (String tableList, String rule);
  public final boolean addRule (String rule) {
    synchronized (Louis.NATIVE_LOCK) {
      return addRule(tableList, rule);
    }
  }

  public final static File[] getAllFiles () {
    String[] tables = Metadata.listTables();
    if (tables == null) return null;

    int count = tables.length;
    File[] files = new File[count];

    for (int index=0; index<count; index+=1) {
      files[index] = new File(tables[index]);
    }

    return files;
  }
}
