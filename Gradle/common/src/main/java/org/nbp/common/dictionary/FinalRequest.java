package org.nbp.common.dictionary;

public abstract class FinalRequest extends DictionaryRequest {
  protected FinalRequest (String... operands) {
    super(operands);
  }

  @Override
  public final boolean isFinal () {
    return true;
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
