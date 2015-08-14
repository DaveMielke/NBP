package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeechOn extends NextValueAction {
  public SpeechOn (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechEnabledControl(), false);
  }
}
