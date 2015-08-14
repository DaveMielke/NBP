package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakSofter extends PreviousValueAction {
  public SpeakSofter (Endpoint endpoint) {
    super(endpoint, Controls.getSpeechVolumeControl(), false);
  }
}
