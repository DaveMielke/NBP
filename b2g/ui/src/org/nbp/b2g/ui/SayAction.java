package org.nbp.b2g.ui;

public abstract class SayAction extends Action {
  protected abstract CharSequence getText (Endpoint endpoint);

  @Override
  public boolean performAction () {
    CharSequence text;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        text = getText(endpoint);
      }
    }

    {
      SpeechDevice speech = Devices.speech.get();

      synchronized (speech) {
        speech.stopSpeaking();
        return speech.say(text);
      }
    }
  }

  protected SayAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
