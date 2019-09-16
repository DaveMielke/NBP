package org.nbp.common.dictionary;

public class MatchEntry {
  private final String matchedWord;
  private final String databaseName;

  public MatchEntry (String word, String database) {
    matchedWord = word;
    databaseName = database;
  }

  public final String getWord () {
    return matchedWord;
  }

  public final String getDatabase () {
    return databaseName;
  }
}
