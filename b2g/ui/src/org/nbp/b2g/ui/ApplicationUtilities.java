package org.nbp.b2g.ui;

import java.io.File;

import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import android.os.Handler;
import android.os.Looper;

import android.view.ViewConfiguration;

public abstract class ApplicationUtilities {
  private final static Handler mainHandler = new Handler(Looper.getMainLooper());

  public final static boolean runOnMainThread (Runnable runnable) {
    return mainHandler.post(runnable);
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

  public static boolean say (CharSequence text, boolean immediate) {
    SpeechDevice speech = Devices.speech.get();

    synchronized (speech) {
      if (immediate) {
        if (!speech.stopSpeaking()) {
          return false;
        }
      }

      return speech.say(text);
    }
  }

  public static boolean say (CharSequence... segments) {
    boolean immediate = true;

    for (CharSequence segment : segments) {
      if (segment == null) continue;
      if (!say(segment, immediate)) return false;
      immediate = false;
    }

    return true;
  }

  public static boolean say (int resource) {
    return say(ApplicationContext.getString(resource));
  }

  public static boolean spell (CharSequence text) {
    int length = text.length();
    CharSequence[] characters = new CharSequence[length];

    for (int index=0; index<length; index+=1) {
      characters[index] = CharacterPhrase.get(text.charAt(index));
    }

    return ApplicationUtilities.say(characters);
  }

  public static void message (String text) {
    Devices.braille.get().message(text);
    say(text);
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

  private final static Object LIBRARY_LOCK = new Object();
  private static boolean libraryLoaded = false;

  public static void loadLibrary () {
    synchronized (LIBRARY_LOCK) {
      if (!libraryLoaded) {
        System.loadLibrary("b2g_ui");
        libraryLoaded = true;
      }
    }
  }

  private ApplicationUtilities () {
  }
}
