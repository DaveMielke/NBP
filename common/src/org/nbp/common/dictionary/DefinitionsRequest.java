package org.nbp.common.dictionary;

public abstract class DefinitionsRequest extends CommandRequest implements DefinitionsHandler {
  private final DefinitionList savedDefinitions = new DefinitionList();

  protected DefinitionsRequest (String... arguments) {
    super(arguments);
  }

  public final DefinitionList getDefinitions () {
    return savedDefinitions;
  }

  @Override
  public void handleDefinitions (DefinitionList definitions) {
  }

  @Override
  protected final void handleResult () {
    handleDefinitions(getDefinitions());
  }

  protected final void saveDefinitions (DictionaryOperands operands) {
    String word = operands.next();
    String name = operands.next();
    String description = operands.next();
    String text = getTextAsString();
    savedDefinitions.add(word, text, name, description);
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.UNKNOWN_DATABASE:
        logProblem("unknown database");
        return true;

      case ResponseCodes.NO_MATCH:
        return true;

      case ResponseCodes.BEGIN_DEFINITION_LIST:
        return false;

      case ResponseCodes.BEGIN_DEFINITION_TEXT:
        saveDefinitions(operands);
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
