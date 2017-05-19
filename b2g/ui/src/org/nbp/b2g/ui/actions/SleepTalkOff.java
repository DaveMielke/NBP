package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SleepTalkOff extends PreviousValueAction {
  public SleepTalkOff (Endpoint endpoint) {
    super(endpoint, Controls.sleepTalk, false);
  }
}
