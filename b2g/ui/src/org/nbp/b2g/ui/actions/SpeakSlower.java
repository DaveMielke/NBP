package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakSlower extends PreviousValueAction {
  public SpeakSlower (Endpoint endpoint) {
    super(endpoint, Controls.speechRate, false);
  }
}
