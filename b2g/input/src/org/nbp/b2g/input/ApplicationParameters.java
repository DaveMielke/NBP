package org.nbp.b2g.input;

import android.os.Build;

public abstract class ApplicationParameters {
  public static volatile boolean MONITOR_KEYBOARD_DIRECTLY = true;
  public static volatile boolean CHORDS_SEND_SYSTEM_KEYS = true;
  public static volatile boolean CHORDS_SEND_ARROW_KEYS = true;

  public static volatile boolean LOG_KEY_EVENTS = false;
  public static volatile boolean LOG_PERFORMED_ACTIONS = false;
  public static volatile boolean LOG_SCREEN_NAVIGATION = false;
  public static volatile boolean LOG_ACCESSIBILITY_EVENTS = false;

  public static volatile long LONG_PRESS_DELAY = 100; // milliseconds
  public static volatile long SCROLL_DELAY = 500; // milliseconds

  public static volatile int SDK_VERSION = Build.VERSION.SDK_INT;

  public static volatile String CLOCK_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
  public static volatile long CLOCK_UPDATE_INTERVAL = 1000; // milliseconds

  public static volatile int BEEP_VOLUME = 100; // percentage
  public static volatile int BEEP_DURATION = 100; // milliseconds

  private ApplicationParameters () {
  }
}
