package org.nbp.b2g.ui;

public abstract class SpeechAction extends Action {
  protected SpeechDevice getSpeechDevice () {
    return Devices.getSpeechDevice();
  }

  protected SpeechAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
