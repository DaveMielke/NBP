package org.nbp.common.dictionary;

public abstract class CommandResponse extends DictionaryResponse {
  protected CommandResponse (String... operands) {
    super(operands);
  }
}
