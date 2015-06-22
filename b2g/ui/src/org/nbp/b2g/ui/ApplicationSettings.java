package org.nbp.b2g.ui;

public abstract class ApplicationSettings {
  public static volatile boolean LONG_PRESS = ApplicationParameters.DEFAULT_LONG_PRESS;
  public static volatile boolean REVERSE_PANNING = ApplicationParameters.DEFAULT_REVERSE_PANNING;
  public static volatile boolean ONE_HAND = ApplicationParameters.DEFAULT_ONE_HAND;

  public static volatile boolean SPEECH_ON = ApplicationParameters.DEFAULT_SPEECH_ON;
  public static volatile float SPEECH_VOLUME = ApplicationParameters.DEFAULT_SPEECH_VOLUME;
  public static volatile float SPEECH_BALANCE = ApplicationParameters.DEFAULT_SPEECH_BALANCE;
  public static volatile float SPEECH_RATE = ApplicationParameters.DEFAULT_SPEECH_RATE;
  public static volatile float SPEECH_PITCH = ApplicationParameters.DEFAULT_SPEECH_PITCH;

  public static volatile boolean DEVELOPER_ACTIONS = ApplicationParameters.DEFAULT_DEVELOPER_ACTIONS;
  public static volatile boolean LOG_KEYS = ApplicationParameters.DEFAULT_LOG_KEYS;
  public static volatile boolean LOG_ACTIONS = ApplicationParameters.DEFAULT_LOG_ACTIONS;
  public static volatile boolean LOG_NAVIGATION = ApplicationParameters.DEFAULT_LOG_NAVIGATION;
  public static volatile boolean LOG_UPDATES = ApplicationParameters.DEFAULT_LOG_UPDATES;

  private ApplicationSettings () {
  }
}
