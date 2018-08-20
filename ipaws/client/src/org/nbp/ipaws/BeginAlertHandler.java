package org.nbp.ipaws;

public class BeginAlertHandler extends ResponseHandler {
  private final ResponseReader responseReader;

  public BeginAlertHandler (ResponseReader reader) {
    super();
    responseReader = reader;
  }

  @Override
  public final void handleResponse (String response) {
    String[] operands = getOperands(response, 2);
    int count = operands.length;
    int index = 0;

    String beginIdentifier = "";
    if (index < count) beginIdentifier = operands[index++];

    StringBuilder alert = new StringBuilder();

    while (true) {
      String line = responseReader.readResponse();
      if (line == null) return;

      operands = getOperands(line, 3);
      count = operands.length;
      index = 0;

      String command = "";
      if (index < count) command = operands[index++];

      if (command.equals("endAlert")) {
        String endIdentifier = "";
        if (index < count) endIdentifier = operands[index++];

        if (beginIdentifier.equals(endIdentifier)) {
          Alerts.add(beginIdentifier, alert.toString());
          return;
        }
      }

      alert.append(line);
      alert.append('\n');
    }
  }
}
