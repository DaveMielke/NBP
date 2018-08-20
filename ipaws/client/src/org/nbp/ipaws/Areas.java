package org.nbp.ipaws;

import java.util.List;
import java.util.ArrayList;

public abstract class Areas extends AlertComponent {
  private Areas () {
    super();
  }

  public static class State {
    private final String stateAbbreviation;
    private final String stateName;
    private final String stateSAME;

    public State (String abbreviation, String name, String SAME) {
      stateAbbreviation = abbreviation;
      stateName = name;
      stateSAME = SAME;
    }

    public final String getAbbreviation () {
      return stateAbbreviation;
    }

    public final String getName () {
      return stateName;
    }

    public final String getSAME () {
      return stateSAME;
    }
  }

  private final static List<State> states =
              new ArrayList<State>();

  public static List<State> getStates () {
    return states;
  }
}
