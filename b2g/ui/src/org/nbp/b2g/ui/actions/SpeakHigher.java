package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpeakHigher extends NextValueAction {
  public SpeakHigher (Endpoint endpoint) {
    super(endpoint, Controls.speechPitch, false);
  }
}
