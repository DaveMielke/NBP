package org.nbp.common.dictionary;

public abstract class DefinitionsResponse extends CommandResponse {
  private final DefinitionList definitions = new DefinitionList();

  protected DefinitionsResponse (String... arguments) {
    super(arguments);
  }

  public final DefinitionList getDefinitions () {
    return definitions;
  }

  protected void handleResult (DefinitionList definitions) {
  }

  @Override
  protected final void handleResult () {
    handleResult(getDefinitions());
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
        String description = operands.next();
        String text = getTextAsString();

        definitions.add(word, text, name, description);
        return false;
      }

      default:
        return super.handleResponse(code, operands);
    }
  }
}
