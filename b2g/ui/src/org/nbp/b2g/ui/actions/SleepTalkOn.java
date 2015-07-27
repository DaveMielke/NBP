package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SleepTalkOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getSleepTalkControl().nextValue();
  }

  public SleepTalkOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
