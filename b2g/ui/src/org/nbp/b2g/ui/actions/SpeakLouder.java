package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakLouder extends NextValueAction {
  public SpeakLouder (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechVolumeControl(), false);
  }
}
