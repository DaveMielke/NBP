package org.nbp.b2g.ui;

import org.nbp.common.speech.TextPlayer;
import org.nbp.common.controls.Control;

public class SpeechDevice extends TextPlayer {
  public SpeechDevice () {
    super();
  }

  @Override
  protected final boolean isLogging () {
    return ApplicationSettings.LOG_SPEECH;
  }

  @Override
  protected final boolean isActive () {
    if (!super.isActive()) return false;
    if (!ApplicationSettings.SPEECH_ENABLED) return false;
    if (ApplicationSettings.SLEEP_TALK) return true;
    return ApplicationContext.isAwake();
  }

  @Override
  protected final String getEngineName () {
    return ApplicationSettings.SPEECH_ENGINE;
  }

  @Override
  protected final void initializeProperties () {
    setVolume(ApplicationSettings.SPEECH_VOLUME);
    setRate(ApplicationSettings.SPEECH_RATE);
    setPitch(ApplicationSettings.SPEECH_PITCH);
    setBalance(ApplicationSettings.SPEECH_BALANCE);
  }
}
