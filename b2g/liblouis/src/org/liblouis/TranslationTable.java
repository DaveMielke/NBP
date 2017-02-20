package org.liblouis;

import java.io.File;

public class TranslationTable {
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

  public final TableFile getTable () {
    return getForwardTable();
  }

  public final String getTableName () {
    return getTable().getTableName();
  }

  public final String getFileName () {
    return getTable().getFileName();
  }

  public final File getFileObject () {
    return getTable().getFileObject();
  }

  public final short getEmphasisBit (String name) {
    return getTable().getEmphasisBit(name);
  }
}
