package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLower extends PreviousValueAction {
  public SpeakLower (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechPitchControl(), false);
  }
}
