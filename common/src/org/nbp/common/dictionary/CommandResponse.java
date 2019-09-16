package org.nbp.common.dictionary;

public abstract class CommandResponse extends DictionaryResponse {
  protected CommandResponse (String... operands) {
    super(operands);
  }

  protected final String getTextAsString () {
    DictionaryConnection connection = getConnection();
    StringBuilder text = new StringBuilder();

    while (true) {
      String line = connection.readLine();

      if (line == null) {
        throw new ResponseException("unexpected end of text");
      }

      if (!line.isEmpty()) {
        if (line.charAt(0) == '.') {
          if (line.length() == 1) break;
          line = line.substring(1);
        }
      }

      if (text.length() > 0) text.append('\n');
      text.append(line);
    }

    return text.toString();
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.END_RESPONSE:
        return true;

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
