package org.nbp.common.dictionary;

public class DefineCommand extends DefinitionResponse {
  public DefineCommand (String word, String database) {
    super("define", database, word);
  }

  public DefineCommand (String word) {
    super(word, DictionaryConstants.DATABASE_ALL);
  }
}
