package org.nbp.common.dictionary;

public class MatchCommand extends MatchesResponse {
  public MatchCommand (String word, String strategy, String database) {
    super("match", database, strategy, word);
  }

  public MatchCommand (String word, String strategy) {
    this(word, strategy, DictionaryConstants.DATABASE_ALL);
  }

  public MatchCommand (String word) {
    this(word, DictionaryConstants.STRATEGY_DEFAULT);
  }
}
