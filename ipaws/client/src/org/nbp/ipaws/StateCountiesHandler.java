package org.nbp.ipaws;

import java.util.List;

public class StateCountiesHandler extends ResponseHandler {
  public StateCountiesHandler () {
    super();
  }

  @Override
  public final void handleResponse (String response) {
    String[] operands = getOperands(response, 2);
    int count = operands.length;
    int index = 0;

    if (index == count) return;
    String abbreviation = operands[index++];

    Areas.State state = Areas.getState(abbreviation);
    if (state == null) return;
    List<Areas.County> counties = state.getCounties();

    if (index < count) {
      synchronized (counties) {
        for (String county : getOperands(operands[index++], ",")) {
          count = 2;
          operands = getOperands(county, count);

          if (operands.length == count) {
            index = 0;
            String SAME = operands[index++];
            String name = operands[index++];

            counties.add(new Areas.County(state, name, SAME));
          }
        }

        counties.notify();
      }
    }
  }
}
