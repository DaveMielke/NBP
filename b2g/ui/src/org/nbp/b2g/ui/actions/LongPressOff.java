package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LongPressOff extends PreviousValueAction {
  public LongPressOff (Endpoint endpoint) {
    super(endpoint, Controls.longPress, false);
  }
}
