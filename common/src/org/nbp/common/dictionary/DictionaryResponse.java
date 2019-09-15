package org.nbp.common.dictionary;

public abstract class DictionaryResponse {
  protected DictionaryResponse (String... operands) {
    DictionaryConnection.singleton().startCommand(this, operands);
  }
}
