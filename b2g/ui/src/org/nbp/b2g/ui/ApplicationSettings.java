package org.nbp.b2g.ui;

public abstract class ApplicationSettings {
  public static volatile boolean WORD_WRAP = ApplicationDefaults.WORD_WRAP;

  public static volatile boolean LONG_PRESS = ApplicationDefaults.LONG_PRESS;
  public static volatile boolean REVERSE_PANNING = ApplicationDefaults.REVERSE_PANNING;
  public static volatile boolean ONE_HAND = ApplicationDefaults.ONE_HAND;

  public static volatile TypingMode TYPING_MODE = ApplicationDefaults.TYPING_MODE;
  public static volatile boolean TYPING_BOLD = ApplicationDefaults.TYPING_BOLD;
  public static volatile boolean TYPING_ITALIC = ApplicationDefaults.TYPING_ITALIC;
  public static volatile boolean TYPING_STRIKE = ApplicationDefaults.TYPING_STRIKE;
  public static volatile boolean TYPING_UNDERLINE = ApplicationDefaults.TYPING_UNDERLINE;

  public static volatile boolean LITERARY_BRAILLE = ApplicationDefaults.LITERARY_BRAILLE;
  public static volatile BrailleCode BRAILLE_CODE = ApplicationDefaults.BRAILLE_CODE;

  public static volatile IndicatorOverlay CURSOR_INDICATOR = ApplicationDefaults.CURSOR_INDICATOR;
  public static volatile IndicatorOverlay SELECTION_INDICATOR = ApplicationDefaults.CURSOR_INDICATOR;

  public static volatile boolean BRAILLE_ENABLED = ApplicationDefaults.BRAILLE_ENABLED;
  public static volatile int BRAILLE_FIRMNESS = ApplicationDefaults.BRAILLE_FIRMNESS;
  public static volatile boolean BRAILLE_MONITOR = ApplicationDefaults.BRAILLE_MONITOR;

  public static volatile boolean BRAILLE_DISPLAY = ApplicationDefaults.BRAILLE_DISPLAY;

  public static volatile boolean SPEECH_ENABLED = ApplicationDefaults.SPEECH_ENABLED;
  public static volatile boolean SLEEP_TALK = ApplicationDefaults.SLEEP_TALK;
  public static volatile float SPEECH_VOLUME = ApplicationDefaults.SPEECH_VOLUME;
  public static volatile float SPEECH_BALANCE = ApplicationDefaults.SPEECH_BALANCE;
  public static volatile float SPEECH_RATE = ApplicationDefaults.SPEECH_RATE;
  public static volatile float SPEECH_PITCH = ApplicationDefaults.SPEECH_PITCH;

  public static volatile boolean DEVELOPER_ENABLED = ApplicationDefaults.DEVELOPER_ENABLED;
  public static volatile boolean LOG_UPDATES = ApplicationDefaults.LOG_UPDATES;
  public static volatile boolean LOG_KEYBOARD = ApplicationDefaults.LOG_KEYBOARD;
  public static volatile boolean LOG_ACTIONS = ApplicationDefaults.LOG_ACTIONS;
  public static volatile boolean LOG_NAVIGATION = ApplicationDefaults.LOG_NAVIGATION;
  public static volatile boolean LOG_GESTURES = ApplicationDefaults.LOG_GESTURES;
  public static volatile boolean LOG_BRAILLE = ApplicationDefaults.LOG_BRAILLE;
  public static volatile boolean LOG_SPEECH = ApplicationDefaults.LOG_SPEECH;

  private ApplicationSettings () {
  }
}
