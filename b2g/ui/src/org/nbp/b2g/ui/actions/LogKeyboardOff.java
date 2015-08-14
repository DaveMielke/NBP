package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogKeyboardOff extends PreviousValueAction {
  public LogKeyboardOff (Endpoint endpoint) {
    super(endpoint, Controls.getLogKeyboardControl(), true);
  }
}
