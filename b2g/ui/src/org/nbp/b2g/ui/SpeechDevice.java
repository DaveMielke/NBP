package org.nbp.b2g.ui;

import org.nbp.common.speech.TextPlayer;
import org.nbp.common.Control;

public class SpeechDevice extends TextPlayer {
  public SpeechDevice () {
    super();
  }

  @Override
  protected boolean isLogging () {
    return ApplicationSettings.LOG_SPEECH;
  }

  @Override
  protected boolean isActive () {
    if (!super.isActive()) return false;
    if (!ApplicationSettings.SPEECH_ENABLED) return false;
    if (ApplicationSettings.SLEEP_TALK) return true;
    return ApplicationContext.isAwake();
  }

  @Override
  protected final void initializeProperties () {
    Control.restoreCurrentValues(
      Controls.speechVolume,
      Controls.speechRate,
      Controls.speechPitch,
      Controls.speechBalance
    );
  }
}
