package org.nbp.common.dictionary;

public class QuitCommand extends CommandResponse {
  public QuitCommand () {
    super("quit");
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.SERVER_DISCONNECTING:
        getConnection().close();
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
