package org.nbp.common.dictionary;

public class MatchEntry {
  private final String matchedWord;
  private final String databaseName;

  public MatchEntry (String word, String name) {
    matchedWord = word;
    databaseName = name;
  }

  public final String getMatchedWord () {
    return matchedWord;
  }

  public final String getDatabaseName () {
    return databaseName;
  }
}
