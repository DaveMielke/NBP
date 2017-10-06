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

  public final TableFile getForwardTableFile () {
    return forwardTable;
  }

  public final TableFile getBackwardTableFile () {
    return backwardTable;
  }

  public final TableFile getTableFile () {
    return getForwardTableFile();
  }

  public final String getTableName () {
    return getTableFile().getTableName();
  }

  public final String getFileName () {
    return getTableFile().getFileName();
  }

  public final File getFileObject () {
    return getTableFile().getFileObject();
  }

  public final short getEmphasisBit (String name) {
    return getTableFile().getEmphasisBit(name);
  }
}
