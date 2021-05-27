package org.nbp.common;

import android.media.ToneGenerator;
import android.media.AudioManager;

public abstract class Tones {
  private Tones () {
  }

  private final static Object LOCK = new Object();
  private static ToneGenerator toneGenerator = null;

  public final static ToneGenerator getToneGenerator () {
    synchronized (LOCK) {
      if (toneGenerator == null) {
        toneGenerator = new ToneGenerator(
          AudioManager.STREAM_NOTIFICATION,
          CommonParameters.TONE_VOLUME
        );
      }
    }

    return toneGenerator;
  }

  public final static void beep () {
    getToneGenerator().startTone(
      ToneGenerator.TONE_PROP_BEEP,
      CommonParameters.BEEP_DURATION
    );
  }

  public final static void alert () {
    getToneGenerator().startTone(
      ToneGenerator.TONE_PROP_NACK,
      CommonParameters.ALERT_DURATION
    );
  }
}
