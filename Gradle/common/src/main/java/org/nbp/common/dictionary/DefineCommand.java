package org.nbp.common.dictionary;

public class DefineCommand extends DefinitionsRequest {
  public DefineCommand (String word, String database) {
    super("define", database, word);
  }

  public DefineCommand (String word, DictionaryDatabase database) {
    this(word, database.getName());
  }

  public DefineCommand (String word, boolean all) {
    this(word, (all? DictionaryDatabase.ALL: DictionaryDatabase.FIRST));
  }

  public DefineCommand (String word) {
    this(word, true);
  }
}
