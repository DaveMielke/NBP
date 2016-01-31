package org.nbp.b2g.ui;

import android.os.Build;

import java.nio.charset.Charset;

import org.liblouis.Louis;

public abstract class ApplicationParameters {
  public final static TypingMode DEFAULT_TYPING_MODE = TypingMode.TEXT;
  public final static boolean DEFAULT_TYPING_BOLD = false;
  public final static boolean DEFAULT_TYPING_ITALIC = false;
  public final static boolean DEFAULT_TYPING_UNDERLINE = false;
  public final static boolean DEFAULT_LONG_PRESS = true;
  public final static boolean DEFAULT_REVERSE_PANNING = false;
  public final static boolean DEFAULT_ONE_HAND = false;

  public final static boolean DEFAULT_LITERARY_BRAILLE = true;
  public final static BrailleCode DEFAULT_BRAILLE_CODE = BrailleCode.EN_UEB_G2;

  public final static IndicatorOverlay DEFAULT_CURSOR_INDICATOR = IndicatorOverlay.DOTS_78;
  public final static IndicatorOverlay DEFAULT_SELECTION_INDICATOR = IndicatorOverlay.DOT_8;

  public final static boolean DEFAULT_BRAILLE_ENABLED = true;
  public final static int DEFAULT_BRAILLE_FIRMNESS = 4;
  public final static boolean DEFAULT_BRAILLE_MONITOR = false;

  public final static boolean DEFAULT_SPEECH_ENABLED = true;
  public final static boolean DEFAULT_SLEEP_TALK = false;
  public final static float DEFAULT_SPEECH_VOLUME = 1.0f;
  public final static float DEFAULT_SPEECH_BALANCE = 0.0f;
  public final static float DEFAULT_SPEECH_RATE = 1.0f;
  public final static float DEFAULT_SPEECH_PITCH = 1.0f;

  public final static boolean DEFAULT_DEVELOPER_ENABLED = false;
  public final static boolean DEFAULT_LOG_UPDATES = false;
  public final static boolean DEFAULT_LOG_KEYBOARD = false;
  public final static boolean DEFAULT_LOG_ACTIONS = false;
  public final static boolean DEFAULT_LOG_NAVIGATION = false;
  public final static boolean DEFAULT_LOG_GESTURES = false;
  public final static boolean DEFAULT_LOG_BRAILLE = false;

  public final static boolean ENABLE_SPEECH_DEVICE = true;
  public final static boolean ENABLE_KEYBOARD_MONITOR = true;
  public final static boolean ENABLE_POWER_BUTTON_MONITOR = true;
  public final static boolean ENABLE_BLUETOOTH_SERVER = false;

  public final static int SCREEN_LEFT_OFFSET = 60; // DIPs
  public final static boolean CHORDS_SEND_SYSTEM_KEYS = true;

  public final static long LONG_PRESS_DELAY = 100; // milliseconds
  public final static long VIEW_SCROLL_TIMEOUT = 5000; // milliseconds

  public final static long LONG_PRESS_TIME = 500; // milliseconds
  public final static long INTERMEDIATE_ACTION_TIMEOUT = 5000; // milliseconds

  public final static long TAP_HOLD_TIME = 45; // milliseconds
  public final static long TAP_WAIT_TIME = 100; // milliseconds

  public final static double SWIPE_STEP_DISTANCE = 10.0; // pixels
  public final static long SWIPE_STEP_INTERVAL = 10; // milliseconds
  public final static long SWIPE_HOLD_DURATION = 100; // milliseconds

  public final static int TONE_VOLUME = 100; // percentage
  public final static int BEEP_DURATION = 100; // milliseconds
  public final static int ALERT_DURATION = 300; // milliseconds

  public final static String CLOCK_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss\nccc, MMM d\nzzz (ZZZZ)";
  public final static long CLOCK_UPDATE_INTERVAL = 1000; // milliseconds

  public final static String BLUETOOTH_SERVICE_NAME = "Braille Display";
  public final static boolean BLUETOOTH_SECURE_CONNECTION = true;
  public final static long BLUETOOTH_RETRY_INTERVAL = 10000; // milliseconds
  public final static long BLUETOOTH_READ_TIMEOUT = 1000; // milliseconds

  public final static int BRAILLE_SCROLL_KEEP = 3; // cells
  public final static long BRAILLE_MESSAGE_DURATION = 2000; // milliseconds
  public final static long BRAILLE_POPUP_TIMEOUT = 30000; // milliseconds
  public final static long BRAILLE_WRITE_DELAY = 40; // milliseconds
  public final static long BRAILLE_REWRITE_DELAY = 50; // milliseconds

  public final static byte BRAILLE_CHARACTER_UNDEFINED =
    BrailleDevice.DOT_3|
    BrailleDevice.DOT_6|
    BrailleDevice.DOT_7|
    BrailleDevice.DOT_8;

  public final static long SPEECH_RETRY_DELAY = 5000; // milliseconds

  public final static long MAINTENANCE_REBOOT_DELAY = 1000; // milliseconds

  public final static int SDK_VERSION = Build.VERSION.SDK_INT;

  public final static String INPUT_ENCODING_NAME = "UTF8";
  public final static Charset INPUT_ENCODING_CHARSET = Charset.forName(INPUT_ENCODING_NAME);

  public final static Louis.LogLevel LIBLOUIS_LOG_LEVEL = Louis.LogLevel.INFO;

  private ApplicationParameters () {
  }
}
