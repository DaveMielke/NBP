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

  public static long getTapTimeout () {
    return ViewConfiguration.getTapTimeout();
  }

  public static long getLongPressTimeout () {
    return ViewConfiguration.getLongPressTimeout();
  }

  public static long getGlobalActionTimeout () {
    return ViewConfiguration.getGlobalActionKeyTimeout();
  }

  private final static ToneGenerator toneGenerator = new ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    ApplicationParameters.TONE_VOLUME
  );

  public static void beep () {
    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, ApplicationParameters.BEEP_DURATION);
  }

  public static void alert () {
    toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, ApplicationParameters.ALERT_DURATION);
  }

  public static void message (Endpoint endpoint, String text) {
    BrailleDevice.write(endpoint, text);

    SpeechDevice speech = Devices.getSpeechDevice();
    synchronized (speech) {
      speech.stopSpeaking();
      speech.say(text);
    }
  }

  public static void message (String text) {
    message(Endpoints.getCurrentEndpoint(), text);
  }

  public static void message (String format, Object... arguments) {
    message(String.format(format, arguments));
  }

  public static void message (StringBuilder sb) {
    message(sb.toString());
  }

  public static void message (int resource) {
    message(ApplicationContext.getString(resource));
  }

  public static void message (double index, double count) {
    StringBuilder sb = new StringBuilder();
    sb.append(Math.round((index / count) * 100.0));
    sb.append('%');
    message(sb);
  }

  public static void message (int index, int count) {
    message((double)index, (double)count);
  }

  private ApplicationUtilities () {
  }
}
