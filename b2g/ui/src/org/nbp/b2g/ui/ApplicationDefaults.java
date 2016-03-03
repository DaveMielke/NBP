package org.nbp.b2g.ui;

public abstract class ApplicationDefaults {
  public final static boolean LONG_PRESS = true;
  public final static boolean REVERSE_PANNING = false;
  public final static boolean ONE_HAND = false;

  public final static TypingMode TYPING_MODE = TypingMode.TEXT;
  public final static boolean TYPING_BOLD = false;
  public final static boolean TYPING_ITALIC = false;
  public final static boolean TYPING_STRIKE = false;
  public final static boolean TYPING_UNDERLINE = false;

  public final static boolean LITERARY_BRAILLE = true;
  public final static BrailleCode BRAILLE_CODE = BrailleCode.EN_UEB_G2;

  public final static IndicatorOverlay CURSOR_INDICATOR = IndicatorOverlay.DOTS_78;
  public final static IndicatorOverlay SELECTION_INDICATOR = IndicatorOverlay.DOT_8;

  public final static boolean BRAILLE_ENABLED = true;
  public final static int BRAILLE_FIRMNESS = 4;
  public final static boolean BRAILLE_MONITOR = false;

  public final static boolean SPEECH_ENABLED = true;
  public final static boolean SLEEP_TALK = false;
  public final static float SPEECH_VOLUME = 1.0f;
  public final static float SPEECH_BALANCE = 0.0f;
  public final static float SPEECH_RATE = 1.0f;
  public final static float SPEECH_PITCH = 1.0f;

  public final static boolean DEVELOPER_ENABLED = false;
  public final static boolean LOG_UPDATES = false;
  public final static boolean LOG_KEYBOARD = false;
  public final static boolean LOG_ACTIONS = false;
  public final static boolean LOG_NAVIGATION = false;
  public final static boolean LOG_GESTURES = false;
  public final static boolean LOG_BRAILLE = false;
  public final static boolean LOG_SPEECH = false;

  private ApplicationDefaults () {
  }
}
