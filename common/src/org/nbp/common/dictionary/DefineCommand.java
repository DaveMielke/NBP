package org.nbp.common.dictionary;

public class DefineCommand extends DefinitionResponse {
  public DefineCommand (String word) {
    super("define", "*", word);
  }
}
