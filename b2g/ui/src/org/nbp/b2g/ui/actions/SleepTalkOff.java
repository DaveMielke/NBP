package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SleepTalkOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getSleepTalkControl().previousValue();
  }

  public SleepTalkOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
