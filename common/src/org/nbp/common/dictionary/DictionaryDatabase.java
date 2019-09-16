package org.nbp.common.dictionary;

public enum DictionaryDatabase {
  ALL("*", "search all databases"),
  FIRST("!", "search the first database that contains a match"),
  ; // end of enumeration

  private final String databaseName;
  private final String databaseDescription;

  DictionaryDatabase (String name, String description) {
    databaseName = name;
    databaseDescription = description;
  }

  public final String getName () {
    return databaseName;
  }

  public final String getDescription () {
    return databaseDescription;
  }
}
