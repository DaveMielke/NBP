package org.nbp.b2g.input;

import android.os.SystemClock;

import android.view.ViewConfiguration;

import android.media.ToneGenerator;
import android.media.AudioManager;

public class ApplicationUtilities {
  public static void sleep (long duration) {
    SystemClock.sleep(duration);
  }

  public static long getLongPressTimeout () {
    return ViewConfiguration.getLongPressTimeout();
  }

  public static long getGlobalActionTimeout () {
    return ViewConfiguration.getGlobalActionKeyTimeout();
  }

  public static void beep () {
    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    tg.startTone(ToneGenerator.TONE_PROP_BEEP, 100);
  }

  private ApplicationUtilities () {
  }
}
