package org.nbp.ipaws;

public class AllStatesHandler extends ResponseHandler {
  public AllStatesHandler () {
    super();
  }

  @Override
  public final void handleResponse (String response) {
    for (String state : getOperands(response, ",")) {
      final int count = 3;
      String[] operands = getOperands(state, count);

      if (operands.length == count) {
        int index = 0;
        String abbreviation = operands[index++];
        String SAME = operands[index++];
        String name = operands[index++];
      }
    }
  }
}
