package org.nbp.common.dictionary;

public abstract class DictionaryResponse {
  protected DictionaryResponse (String... arguments) {
    DictionaryConnection.singleton().startCommand(this, arguments);
  }
}
