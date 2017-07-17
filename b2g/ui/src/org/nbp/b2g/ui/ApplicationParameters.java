package org.nbp.b2g.ui;

import org.nbp.common.CommonParameters;
import org.nbp.common.Braille;

import android.os.Build;

import org.liblouis.Louis;

public abstract class ApplicationParameters extends CommonParameters {
  public final static char PASSWORD_CHARACTER = '*';

  public final static long LONG_PRESS_TIME = 500; // milliseconds
  public final static long PARTIAL_ENTRY_TIMEOUT = 2000; // milliseconds

  public final static long BRAILLE_MESSAGE_DURATION = 2000; // milliseconds
  public final static long BRAILLE_POPUP_TIMEOUT = 30000; // milliseconds
  public final static int BRAILLE_SCROLL_KEEP = 3; // cells

  public final static long MAINTENANCE_REBOOT_DELAY = 1000; // milliseconds
  public final static Louis.LogLevel LIBLOUIS_LOG_LEVEL = Louis.LogLevel.INFO;

  public final static boolean ENABLE_KEYBOARD_MONITOR = true;
  public final static boolean ENABLE_POWER_BUTTON_MONITOR = true;
  public final static boolean CHORDS_SEND_SYSTEM_KEYS = true;

  public final static long LONG_PRESS_DELAY = 100; // milliseconds
  public final static long VIEW_SCROLL_TIMEOUT = 5000; // milliseconds
  public final static long TEXT_CHANGE_TIMEOUT = 3000; // milliseconds

  public final static long TAP_HOLD_TIME = 45; // milliseconds
  public final static long TAP_WAIT_TIME = 100; // milliseconds

  public final static double SWIPE_STEP_DISTANCE = 10.0; // pixels
  public final static long SWIPE_STEP_INTERVAL = 10; // milliseconds
  public final static long SWIPE_HOLD_DURATION = 100; // milliseconds

  public final static long CLOCK_UPDATE_INTERVAL = 1000; // milliseconds
  public final static long BATTERY_REPORT_INTERVAL = (28 * 60 * 1000); // milliseconds

  public final static long REMOTE_DISPLAY_READ_TIMEOUT = 100; // milliseconds
  public final static String BLUETOOTH_SERVICE_NAME = "Braille Display";

  public final static long BRAILLE_WRITE_DELAY = 40; // milliseconds
  public final static long BRAILLE_REWRITE_DELAY = 50; // milliseconds

  public final static byte BRAILLE_CHARACTER_UNDEFINED =
    Braille.CELL_DOT_3|
    Braille.CELL_DOT_6|
    Braille.CELL_DOT_7|
    Braille.CELL_DOT_8;

  public final static long SPEECH_RETRY_DELAY = 5000; // milliseconds

  public final static int SDK_VERSION = Build.VERSION.SDK_INT;

  private ApplicationParameters () {
  }
}
