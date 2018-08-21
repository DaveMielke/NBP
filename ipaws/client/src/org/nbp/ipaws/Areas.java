package org.nbp.ipaws;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public abstract class Areas extends ApplicationComponent {
  private Areas () {
    super();
  }

  public static class County {
    private final State countyState;
    private final String countyName;
    private final String areaSAME;

    public County (State state, String name, String SAME) {
      countyState = state;
      countyName = name;
      areaSAME = SAME;
      areaToCounty.put(SAME, this);
    }

    public final State getState () {
      return countyState;
    }

    public final String getName () {
      return countyName;
    }

    public final String getSAME () {
      return areaSAME;
    }
  }

  public static class State {
    private final String stateAbbreviation;
    private final String stateName;
    private final String areaSAME;

    public State (String abbreviation, String name, String SAME) {
      stateAbbreviation = abbreviation;
      stateName = name;
      areaSAME = SAME;
      abbreviationToState.put(abbreviation, this);
      areaToState.put(SAME, this);
    }

    public final String getAbbreviation () {
      return stateAbbreviation;
    }

    public final String getName () {
      return stateName;
    }

    public final String getSAME () {
      return areaSAME;
    }

    private final List<County> countyList =
         new ArrayList<County>();

    public final List<County> getCounties () {
      return countyList;
    }
  }

  private final static List<State> stateList =
              new ArrayList<State>();

  public static List<State> getStates () {
    return stateList;
  }

  private final static Map<String, State> abbreviationToState =
               new HashMap<String, State>();

  public static State getStateByAbbreviation (String abbreviation) {
    return abbreviationToState.get(abbreviation);
  }

  private final static Map<String, State> areaToState =
               new HashMap<String, State>();

  public static State getStateByArea (String SAME) {
    return areaToState.get(SAME);
  }

  private final static Map<String, County> areaToCounty =
               new HashMap<String, County>();

  public static County getCountyByArea (String SAME) {
    return areaToCounty.get(SAME);
  }
}
