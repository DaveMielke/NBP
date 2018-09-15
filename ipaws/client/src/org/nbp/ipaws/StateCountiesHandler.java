package org.nbp.ipaws;

import java.util.List;

public class StateCountiesHandler extends CommandHandler {
  public StateCountiesHandler (SessionOperations operations) {
    super(operations);
  }

  @Override
  public final boolean handleOperands (String string) {
    String[] operands = getOperands(string, 2);
    int count = operands.length;
    int index = 0;

    if (index == count) return true;
    String abbreviation = operands[index++];

    Areas.State state = Areas.getStateByAbbreviation(abbreviation);
    if (state == null) return true;
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

    return true;
  }
}
