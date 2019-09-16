package org.nbp.common.dictionary;

public abstract class DefinitionsResponse extends CommandResponse {
  protected DefinitionsResponse (String... arguments) {
    super(arguments);
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.NO_MATCH:
        return true;

      case ResponseCodes.BEGIN_DEFINITION_LIST:
        return false;

      case ResponseCodes.BEGIN_DEFINITION_TEXT: {
        String word = operands.next();
        String name = operands.next();
        String title = operands.next();
        String text = getText();

        return false;
      }

      default:
        return super.handleResponse(code, operands);
    }
  }
}
