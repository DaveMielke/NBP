package org.nbp.b2g.ui;

import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import android.view.ViewConfiguration;

import android.media.ToneGenerator;
import android.media.AudioManager;

import java.io.File;

public abstract class ApplicationUtilities {
  public static boolean haveSdkVersion (int version) {
    return ApplicationParameters.SDK_VERSION >= version;
  }

  public static long getSystemClock () {
    return SystemClock.uptimeMillis();
  }

  public static void sleep (long duration) {
    if (duration > 0) SystemClock.sleep(duration);
  }

  public static File getExternalStorageDirectory () {
    File directory = Environment.getExternalStorageDirectory();
    if (directory == null) return null;
    directory = new File(directory, ApplicationUtilities.class.getPackage().getName());

    if (!directory.exists()) {
      if (!directory.mkdir()) return null;
    }

    return directory;
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

  public static void message (String text) {
    Devices.braille.get().write(text, ApplicationParameters.BRAILLE_MESSAGE_DURATION);

    SpeechDevice speech = Devices.speech.get();
    synchronized (speech) {
      speech.stopSpeaking();
      speech.say(text);
    }
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
