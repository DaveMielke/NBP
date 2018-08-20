package org.nbp.ipaws;

public class RemoveAlertHandler extends ResponseHandler {
  public RemoveAlertHandler () {
    super();
  }

  @Override
  public final void handleResponse (String response) {
    String[] operands = getOperands(response, 2);
    int count = operands.length;
    int index = 0;

    String identifier = "";
    if (index < count) identifier = operands[index++];

    if (!identifier.isEmpty()) {
      Alerts.remove(identifier);
    }
  }
}
