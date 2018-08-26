package org.nbp.ipaws;

import java.util.List;

public class AllStatesHandler extends ResponseHandler {
  public AllStatesHandler () {
    super();
  }

  @Override
  public final boolean handleResponse (String response) {
    List<Areas.State> states = Areas.getStates();

    synchronized (states) {
      for (String state : getOperands(response, ",")) {
        final int count = 3;
        String[] operands = getOperands(state, count);

        if (operands.length == count) {
          int index = 0;
          String abbreviation = operands[index++];
          String SAME = operands[index++];
          String name = operands[index++];

          states.add(new Areas.State(abbreviation, name, SAME));
        }
      }

      states.notify();
    }

    return true;
  }
}
