package org.nbp.common.dictionary;

public class ShowInfoCommand extends TextRequest {
  public ShowInfoCommand (String database) {
    super("show", "info", database);
  }

  public ShowInfoCommand (DictionaryDatabase database) {
    this(database.getName());
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.UNKNOWN_DATABASE:
        logProblem("unknown database");
        return true;

      case ResponseCodes.BEGIN_DATABASE_TEXT:
        saveText();
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
