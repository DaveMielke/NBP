package org.nbp.ipaws;

public class BeginAlertHandler extends OperandsHandler {
  private final CommandReader commandReader;

  public BeginAlertHandler (CommandReader reader) {
    super();
    commandReader = reader;
  }

  @Override
  public final boolean handleOperands (String string) {
    String[] operands = getOperands(string, 2);
    int count = operands.length;
    int index = 0;

    String beginIdentifier = "";
    if (index < count) beginIdentifier = operands[index++];

    StringBuilder alert = new StringBuilder();

    while (true) {
      String line = commandReader.readCommand();
      if (line == null) return true;

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
          return true;
        }
      }

      alert.append(line);
      alert.append('\n');
    }
  }
}
