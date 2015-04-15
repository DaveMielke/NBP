package org.nbp.b2g.ui;

public abstract class SpeechAction extends Action {
  protected SpeechDevice getSpeechDevice () {
    return Devices.getSpeechDevice();
  }

  protected boolean adjustVolume (SpeechDevice speech, int steps) {
    return false;
  }

  protected boolean adjustBalance (SpeechDevice speech, int steps) {
    return false;
  }

  protected boolean adjustRate (SpeechDevice speech, int steps) {
    return false;
  }

  protected boolean adjustPitch (SpeechDevice speech, int steps) {
    return false;
  }

  protected SpeechAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
