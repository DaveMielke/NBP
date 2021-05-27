package org.nbp.common.dictionary;

public class DefinitionEntry {
  private final String matchedWord;
  private final String definitionText;
  private final String databaseName;
  private final String databaseDescription;

  public DefinitionEntry (String word, String text, String name, String description) {
    matchedWord = word;
    definitionText = text;
    databaseName = name;
    databaseDescription = description;
  }

  public final String getMatchedWord () {
    return matchedWord;
  }

  public final String getDefinitionText () {
    return definitionText;
  }

  public final String getDatabaseName () {
    return databaseName;
  }

  public final String getDatabaseDescription () {
    return databaseDescription;
  }
}
