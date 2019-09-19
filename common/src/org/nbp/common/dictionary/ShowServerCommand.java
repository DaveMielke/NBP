package org.nbp.common.dictionary;

public class ShowServerCommand extends TextRequest {
  public ShowServerCommand () {
    super("show", "server");
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.BEGIN_SERVER_TEXT:
        saveText();
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
