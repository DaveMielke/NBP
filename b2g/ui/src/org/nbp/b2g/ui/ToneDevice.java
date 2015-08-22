package org.nbp.b2g.ui;

import android.media.ToneGenerator;
import android.media.AudioManager;

public class ToneDevice extends ToneGenerator {
  public final void beep () {
    startTone(ToneGenerator.TONE_PROP_BEEP, ApplicationParameters.BEEP_DURATION);
  }

  public final void alert () {
    startTone(ToneGenerator.TONE_PROP_NACK, ApplicationParameters.ALERT_DURATION);
  }

  public ToneDevice () {
    super(
      AudioManager.STREAM_NOTIFICATION,
      ApplicationParameters.TONE_VOLUME
    );
  }
}
