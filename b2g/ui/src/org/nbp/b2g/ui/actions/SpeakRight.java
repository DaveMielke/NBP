package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakRight extends NextValueAction {
  public SpeakRight (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechBalanceControl(), false);
  }
}
