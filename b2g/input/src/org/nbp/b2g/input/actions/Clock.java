package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

public class Clock extends ActivityAction {
  @Override
  protected Class getActivityClass () {
    return ClockActivity.class;
  }

  public Clock () {
    super();
  }
}
