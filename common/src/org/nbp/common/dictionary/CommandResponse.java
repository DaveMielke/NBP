package org.nbp.common.dictionary;

public abstract class CommandResponse extends DictionaryResponse {
  protected CommandResponse (String... operands) {
    super(operands);
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.UNKNOWN_COMMAND:
        logProblem("unknown command");
        return true;

      case ResponseCodes.UNIMPLEMENTED_COMMAND:
        logProblem("unimplemented command");
        return true;

      case ResponseCodes.UNIMPLEMENTED_PARAMETER:
        logProblem("unimplemented parameter");
        return true;

      case ResponseCodes.ILLEGAL_PARAMETER:
        logProblem("illegal parameter");
        return true;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
