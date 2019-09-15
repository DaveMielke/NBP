package org.nbp.common.dictionary;

public class DefineCommand extends DefinitionsResponse {
  public DefineCommand (String word, String database) {
    super("define", database, word);
  }

  public DefineCommand (String word) {
    this(word, DictionaryConstants.DATABASE_ALL);
  }
}
