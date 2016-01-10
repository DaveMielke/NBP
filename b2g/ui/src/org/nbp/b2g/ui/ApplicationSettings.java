package org.nbp.b2g.ui;

public abstract class ApplicationSettings {
  public static volatile boolean LITERARY_BRAILLE = ApplicationParameters.DEFAULT_LITERARY_BRAILLE;
  public static volatile BrailleCode BRAILLE_CODE = ApplicationParameters.DEFAULT_BRAILLE_CODE;
  public static volatile InputMode INPUT_MODE = ApplicationParameters.DEFAULT_INPUT_MODE;
  public static volatile boolean LONG_PRESS = ApplicationParameters.DEFAULT_LONG_PRESS;
  public static volatile boolean REVERSE_PANNING = ApplicationParameters.DEFAULT_REVERSE_PANNING;
  public static volatile boolean ONE_HAND = ApplicationParameters.DEFAULT_ONE_HAND;

  public static volatile IndicatorOverlay CURSOR_INDICATOR = ApplicationParameters.DEFAULT_CURSOR_INDICATOR;
  public static volatile IndicatorOverlay SELECTION_INDICATOR = ApplicationParameters.DEFAULT_CURSOR_INDICATOR;

  public static volatile boolean BRAILLE_ENABLED = ApplicationParameters.DEFAULT_BRAILLE_ENABLED;
  public static volatile int BRAILLE_FIRMNESS = ApplicationParameters.DEFAULT_BRAILLE_FIRMNESS;
  public static volatile boolean BRAILLE_MONITOR = ApplicationParameters.DEFAULT_BRAILLE_MONITOR;

  public static volatile boolean SPEECH_ENABLED = ApplicationParameters.DEFAULT_SPEECH_ENABLED;
  public static volatile boolean SLEEP_TALK = ApplicationParameters.DEFAULT_SLEEP_TALK;
  public static volatile float SPEECH_VOLUME = ApplicationParameters.DEFAULT_SPEECH_VOLUME;
  public static volatile float SPEECH_BALANCE = ApplicationParameters.DEFAULT_SPEECH_BALANCE;
  public static volatile float SPEECH_RATE = ApplicationParameters.DEFAULT_SPEECH_RATE;
  public static volatile float SPEECH_PITCH = ApplicationParameters.DEFAULT_SPEECH_PITCH;

  public static volatile boolean DEVELOPER_ENABLED = ApplicationParameters.DEFAULT_DEVELOPER_ENABLED;
  public static volatile boolean LOG_UPDATES = ApplicationParameters.DEFAULT_LOG_UPDATES;
  public static volatile boolean LOG_KEYBOARD = ApplicationParameters.DEFAULT_LOG_KEYBOARD;
  public static volatile boolean LOG_ACTIONS = ApplicationParameters.DEFAULT_LOG_ACTIONS;
  public static volatile boolean LOG_NAVIGATION = ApplicationParameters.DEFAULT_LOG_NAVIGATION;
  public static volatile boolean LOG_GESTURES = ApplicationParameters.DEFAULT_LOG_GESTURES;
  public static volatile boolean LOG_BRAILLE = ApplicationParameters.DEFAULT_LOG_BRAILLE;

  private ApplicationSettings () {
  }
}
