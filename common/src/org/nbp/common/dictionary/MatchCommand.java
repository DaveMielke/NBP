package org.nbp.common.dictionary;

public class MatchCommand extends MatchesRequest {
  public MatchCommand (String word, String strategy, String database) {
    super("match", database, strategy, word);
  }

  public MatchCommand (String word, String strategy, DictionaryDatabase database) {
    this(word, strategy, database.getName());
  }

  public MatchCommand (String word, DictionaryStrategy strategy, String database) {
    this(word, strategy.getName(), database);
  }

  public MatchCommand (String word, DictionaryStrategy strategy, DictionaryDatabase database) {
    this(word, strategy.getName(), database.getName());
  }

  public MatchCommand (String word, String strategy) {
    this(word, strategy, DictionaryDatabase.ALL);
  }

  public MatchCommand (String word, DictionaryStrategy strategy) {
    this(word, strategy.getName());
  }

  public MatchCommand (String word, DictionaryDatabase database) {
    this(word, DictionaryStrategy.DEFAULT, database);
  }

  public MatchCommand (String word) {
    this(word, DictionaryStrategy.DEFAULT);
  }
}
