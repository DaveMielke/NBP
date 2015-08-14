package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakFaster extends NextValueAction {
  public SpeakFaster (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechRateControl(), false);
  }
}
