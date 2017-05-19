package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLeft extends PreviousValueAction {
  public SpeakLeft (Endpoint endpoint) {
    super(endpoint, Controls.speechBalance, false);
  }
}
