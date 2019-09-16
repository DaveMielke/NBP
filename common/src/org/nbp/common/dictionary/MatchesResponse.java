package org.nbp.common.dictionary;

public abstract class MatchesResponse extends CommandResponse {
  protected MatchesResponse (String... arguments) {
    super(arguments);
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.NO_MATCH:
        return true;

      case ResponseCodes.BEGIN_MATCH_LIST: {
        String text = getTextAsString();
        return false;
      }

      default:
        return super.handleResponse(code, operands);
    }
  }
}
