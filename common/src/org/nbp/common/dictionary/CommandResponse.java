package org.nbp.common.dictionary;

import java.util.List;
import java.util.ArrayList;

public abstract class CommandResponse extends DictionaryResponse {
  protected CommandResponse (String... operands) {
    super(operands);
  }

  protected interface TextLineProcessor {
    public void processLine (String line);
  }

  protected final void getText (TextLineProcessor processor) {
    DictionaryConnection connection = getConnection();

    while (true) {
      String line = connection.readLine();

      if (line == null) {
        throw new OperandException("unexpected end of text");
      }

      if (!line.isEmpty()) {
        if (line.charAt(0) == '.') {
          if (line.length() == 1) break;
          line = line.substring(1);
        }
      }

      processor.processLine(line);
    }
  }

  protected final String getTextAsString () {
    final StringBuilder text = new StringBuilder();

    getText(
      new TextLineProcessor() {
        @Override
        public void processLine (String line) {
          if (text.length() > 0) text.append('\n');
          text.append(line);
        }
      }
    );

    return text.toString();
  }

  protected final List<String> getTextAsList () {
    final List<String> text = new ArrayList<String>();

    getText(
      new TextLineProcessor() {
        @Override
        public void processLine (String line) {
          text.add(line);
        }
      }
    );

    return text;
  }

  protected final String[] getTextAsArray () {
    List<String> text = getTextAsList();
    return text.toArray(new String[text.size()]);
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
