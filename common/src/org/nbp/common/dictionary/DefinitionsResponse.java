package org.nbp.common.dictionary;

public abstract class DefinitionsResponse extends CommandResponse {
  protected DefinitionsResponse (String... arguments) {
    super(arguments);
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      default:
        return super.handleResponse(code, operands);
    }
  }
}
