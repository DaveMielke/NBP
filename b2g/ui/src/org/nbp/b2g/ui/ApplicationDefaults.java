package org.nbp.b2g.ui;

import org.nbp.common.speech.SpeechParameters;

import org.nbp.common.dictionary.DictionaryDatabase;
import org.nbp.common.dictionary.DictionaryStrategy;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static boolean LITERARY_BRAILLE = true;
  public final static BrailleCode BRAILLE_CODE = BrailleCode.EN_UEB_G2;
  public final static boolean WORD_WRAP = true;
  public final static boolean SHOW_NOTIFICATIONS = true;

  public final static TypingMode TYPING_MODE = TypingMode.TEXT;
  public final static boolean TYPING_BOLD = false;
  public final static boolean TYPING_ITALIC = false;
  public final static boolean TYPING_STRIKE = false;
  public final static boolean TYPING_UNDERLINE = false;

  public final static boolean SHOW_HIGHLIGHTED = true;
  public final static IndicatorOverlay SELECTION_INDICATOR = IndicatorOverlay.DOT_8;
  public final static IndicatorOverlay CURSOR_INDICATOR = IndicatorOverlay.DOTS_78;
  public final static GenericLevel BRAILLE_FIRMNESS = GenericLevel.MEDIUM;
  public final static boolean BRAILLE_MONITOR = false;
  public final static boolean BRAILLE_ENABLED = true;

  public final static boolean SPEECH_ENABLED = true;
  public final static boolean ECHO_WORDS = true;
  public final static boolean ECHO_CHARACTERS = true;
  public final static boolean ECHO_DELETIONS = true;
  public final static boolean ECHO_SELECTION = true;
  public final static boolean SPEAK_LINES = true;
  public final static float SPEECH_VOLUME = SpeechParameters.VOLUME_MAXIMUM;
  public final static float SPEECH_RATE = SpeechParameters.RATE_REFERENCE;
  public final static float SPEECH_PITCH = SpeechParameters.PITCH_REFERENCE;
  public final static float SPEECH_BALANCE = SpeechParameters.BALANCE_CENTER;
  public final static boolean SLEEP_TALK = false;
  public final static String SPEECH_ENGINE = "";

  public final static boolean INPUT_EDITING = true;
  public final static boolean LONG_PRESS = true;
  public final static boolean REVERSE_PANNING = false;

  public final static boolean ONE_HAND = false;
  public final static int SPACE_TIMEOUT = 1000; // milliseconds
  public final static int PRESSED_TIMEOUT = 15000; // milliseconds

  public final static boolean REMOTE_DISPLAY = false;
  public final static boolean SECURE_CONNECTION = false;

  public final static DictionaryDatabase DICTIONARY_DATABASE = DictionaryDatabase.ALL;
  public final static boolean MULTIPLE_DEFINITIONS = false;
  public final static boolean SUGGEST_WORDS = false;
  public final static DictionaryStrategy DICTIONARY_STRATEGY = DictionaryStrategy.DEFAULT;

  public final static ComputerBraille COMPUTER_BRAILLE = ComputerBraille.LOCAL;
  public final static PhoneticAlphabet PHONETIC_ALPHABET = PhoneticAlphabet.OFF;
  public final static ScreenOrientation SCREEN_ORIENTATION = ScreenOrientation.UNLOCKED;

  public final static boolean CRASH_EMAILS = false;
  public final static boolean ADVANCED_ACTIONS = false;
  public final static boolean EXTRA_INDICATORS = false;
  public final static boolean EVENT_MESSAGES = false;
  public final static boolean LOG_UPDATES = false;
  public final static boolean LOG_KEYBOARD = false;
  public final static boolean LOG_ACTIONS = false;
  public final static boolean LOG_NAVIGATION = false;
  public final static boolean LOG_EMULATIONS = false;
  public final static boolean LOG_BRAILLE = false;
  public final static boolean LOG_SPEECH = false;
}
