package org.nbp.b2g.ui;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static boolean LITERARY_BRAILLE = ApplicationDefaults.LITERARY_BRAILLE;
  public volatile static BrailleCode BRAILLE_CODE = ApplicationDefaults.BRAILLE_CODE;
  public volatile static boolean WORD_WRAP = ApplicationDefaults.WORD_WRAP;
  public volatile static boolean SHOW_NOTIFICATIONS = ApplicationDefaults.SHOW_NOTIFICATIONS;

  public volatile static TypingMode TYPING_MODE = ApplicationDefaults.TYPING_MODE;
  public volatile static boolean TYPING_BOLD = ApplicationDefaults.TYPING_BOLD;
  public volatile static boolean TYPING_ITALIC = ApplicationDefaults.TYPING_ITALIC;
  public volatile static boolean TYPING_STRIKE = ApplicationDefaults.TYPING_STRIKE;
  public volatile static boolean TYPING_UNDERLINE = ApplicationDefaults.TYPING_UNDERLINE;

  public volatile static boolean SHOW_HIGHLIGHTED = ApplicationDefaults.SHOW_HIGHLIGHTED;
  public volatile static IndicatorOverlay SELECTION_INDICATOR = ApplicationDefaults.SELECTION_INDICATOR;
  public volatile static IndicatorOverlay CURSOR_INDICATOR = ApplicationDefaults.CURSOR_INDICATOR;
  public volatile static GenericLevel BRAILLE_FIRMNESS = ApplicationDefaults.BRAILLE_FIRMNESS;
  public volatile static boolean BRAILLE_MONITOR = ApplicationDefaults.BRAILLE_MONITOR;
  public volatile static boolean BRAILLE_ENABLED = ApplicationDefaults.BRAILLE_ENABLED;

  public volatile static boolean SPEECH_ENABLED = ApplicationDefaults.SPEECH_ENABLED;
  public volatile static boolean ECHO_WORDS = ApplicationDefaults.ECHO_WORDS;
  public volatile static boolean ECHO_CHARACTERS = ApplicationDefaults.ECHO_CHARACTERS;
  public volatile static boolean ECHO_DELETIONS = ApplicationDefaults.ECHO_DELETIONS;
  public volatile static boolean ECHO_SELECTION = ApplicationDefaults.ECHO_SELECTION;
  public volatile static boolean SPEAK_LINES = ApplicationDefaults.SPEAK_LINES;
  public volatile static float SPEECH_VOLUME = ApplicationDefaults.SPEECH_VOLUME;
  public volatile static float SPEECH_RATE = ApplicationDefaults.SPEECH_RATE;
  public volatile static float SPEECH_PITCH = ApplicationDefaults.SPEECH_PITCH;
  public volatile static float SPEECH_BALANCE = ApplicationDefaults.SPEECH_BALANCE;
  public volatile static boolean SLEEP_TALK = ApplicationDefaults.SLEEP_TALK;

  public volatile static boolean EDITING_ENABLED = ApplicationDefaults.EDITING_ENABLED;
  public volatile static boolean LONG_PRESS = ApplicationDefaults.LONG_PRESS;
  public volatile static boolean REVERSE_PANNING = ApplicationDefaults.REVERSE_PANNING;

  public volatile static boolean ONE_HAND = ApplicationDefaults.ONE_HAND;
  public volatile static int SPACE_TIMEOUT = ApplicationDefaults.SPACE_TIMEOUT;
  public volatile static int PRESSED_TIMEOUT = ApplicationDefaults.PRESSED_TIMEOUT;

  public volatile static boolean REMOTE_DISPLAY = ApplicationDefaults.REMOTE_DISPLAY;
  public volatile static boolean SECURE_CONNECTION = ApplicationDefaults.SECURE_CONNECTION;

  public volatile static ComputerBraille COMPUTER_BRAILLE = ApplicationDefaults.COMPUTER_BRAILLE;
  public volatile static boolean CRASH_EMAILS = ApplicationDefaults.CRASH_EMAILS;
  public volatile static boolean ADVANCED_ACTIONS = ApplicationDefaults.ADVANCED_ACTIONS;
  public volatile static boolean EXTRA_INDICATORS = ApplicationDefaults.EXTRA_INDICATORS;
  public volatile static boolean EVENT_MESSAGES = ApplicationDefaults.EVENT_MESSAGES;
  public volatile static boolean LOG_UPDATES = ApplicationDefaults.LOG_UPDATES;
  public volatile static boolean LOG_KEYBOARD = ApplicationDefaults.LOG_KEYBOARD;
  public volatile static boolean LOG_ACTIONS = ApplicationDefaults.LOG_ACTIONS;
  public volatile static boolean LOG_NAVIGATION = ApplicationDefaults.LOG_NAVIGATION;
  public volatile static boolean LOG_EMULATIONS = ApplicationDefaults.LOG_EMULATIONS;
  public volatile static boolean LOG_BRAILLE = ApplicationDefaults.LOG_BRAILLE;
  public volatile static boolean LOG_SPEECH = ApplicationDefaults.LOG_SPEECH;
}
