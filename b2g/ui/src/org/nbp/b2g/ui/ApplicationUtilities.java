package org.nbp.b2g.ui;

import android.os.Build;
import android.os.SystemClock;

import android.view.ViewConfiguration;

import android.media.ToneGenerator;
import android.media.AudioManager;

public class ApplicationUtilities {
  public static boolean haveSdkVersion (int version) {
    return ApplicationParameters.SDK_VERSION >= version;
  }

  public static void sleep (long duration) {
    SystemClock.sleep(duration);
  }

  public static long getLongPressTimeout () {
    return ViewConfiguration.getLongPressTimeout();
  }

  public static long getGlobalActionTimeout () {
    return ViewConfiguration.getGlobalActionKeyTimeout();
  }

  private final static ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, ApplicationParameters.BEEP_VOLUME);

  public static void beep () {
    tg.startTone(ToneGenerator.TONE_PROP_BEEP, ApplicationParameters.BEEP_DURATION);
  }

  public static void message (Endpoint endpoint, String text) {
    synchronized (endpoint) {
      BrailleDevice.write(endpoint, text);
    }
  }

  public static void message (String text) {
    message(Endpoints.getCurrentEndpoint(), text);
  }

  private ApplicationUtilities () {
  }
}
