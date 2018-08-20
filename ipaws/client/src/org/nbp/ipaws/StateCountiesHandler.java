package org.nbp.ipaws;

public class StateCountiesHandler extends ResponseHandler {
  public StateCountiesHandler () {
    super();
  }

  @Override
  public final void handleResponse (String response) {
    String[] operands = getOperands(response, 2);
    int count = operands.length;
    int index = 0;

    String state = "";
    if (index < count) state = operands[index++];

    String counties = "";
    if (index < count) counties = operands[index++];

    for (String county : getOperands(counties, ",")) {
      count = 2;
      operands = getOperands(county, count);

      if (operands.length == count) {
        index = 0;
        String SAME = operands[index++];
        String name = operands[index++];
      }
    }
  }
}
