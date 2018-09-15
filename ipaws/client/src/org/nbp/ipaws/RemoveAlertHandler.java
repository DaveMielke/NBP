package org.nbp.ipaws;

public class RemoveAlertHandler extends CommandHandler {
  public RemoveAlertHandler (SessionOperations operations) {
    super(operations);
  }

  @Override
  public final boolean handleOperands (String string) {
    String[] operands = getOperands(string, 2);
    int count = operands.length;
    int index = 0;

    String identifier = "";
    if (index < count) identifier = operands[index++];

    if (!identifier.isEmpty()) {
      Alerts.remove(identifier);
    }

    return true;
  }
}
