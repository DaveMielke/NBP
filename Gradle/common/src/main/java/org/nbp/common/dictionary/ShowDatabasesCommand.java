package org.nbp.common.dictionary;

public class ShowDatabasesCommand extends ItemsRequest {
  public ShowDatabasesCommand () {
    super("show", "databases");
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.NO_DATABASES:
        return true;

      case ResponseCodes.BEGIN_DATABASE_LIST:
        saveItems();
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
