package org.nbp.b2g.input;

public abstract class ApplicationParameters {
  public static volatile boolean CHORDS_SEND_SYSTEM_KEYS = true;
  public static volatile boolean CHORDS_SEND_ARROW_KEYS = true;

  public static volatile String CLOCK_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
  public static volatile long CLOCK_UPDATE_INTERVAL = 1000;

  public static volatile boolean LOG_KEY_EVENTS = false;
  public static volatile boolean LOG_ACTIONS = false;

  private ApplicationParameters () {
  }
}
