package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class ScanCode {
  private final static String LOG_TAG = ScanCode.class.getName();

  public final static int POWER;
  public final static int WAKEUP;

  public final static int CURSOR_FIRST;
  public final static int CURSOR_LAST;

  public final static int DOTS        = 0X300;
  public final static int CHORD       = 0X400;

  private static Map<Integer, Integer> scanCodeMap = new HashMap<Integer, Integer>();

  public static int toKeyMask (int code) {
    {
      Integer mask = scanCodeMap.get(code);
      if (mask != null) return mask;
    }

    return 0;
  }

  private static int map (String name, int mask) {
    int code = Keyboard.getScanCodeValue(name);

    if (code != Keyboard.NULL_SCAN_CODE) {
      scanCodeMap.put(code, mask);
    }

    return code;
  }

  private static int map (String name) {
    return map(name, 0);
  }

  static {
    POWER = map("POWER");
    WAKEUP = map("B2G_WAKEUP");

    CURSOR_FIRST = map("B2G_CURSOR_0");
    CURSOR_LAST = map("B2G_CURSOR_39");

    map("VOLUMEDOWN", KeyMask.VOLUME_DOWN);
    map("VOLUMEUP", KeyMask.VOLUME_UP);

    map("BRL_DOT1", KeyMask.DOT_7);
    map("BRL_DOT2", KeyMask.DOT_3);
    map("BRL_DOT3", KeyMask.DOT_2);
    map("BRL_DOT4", KeyMask.DOT_1);
    map("BRL_DOT5", KeyMask.DOT_4);
    map("BRL_DOT6", KeyMask.DOT_5);
    map("BRL_DOT7", KeyMask.DOT_6);
    map("BRL_DOT8", KeyMask.DOT_8);
    map("BRL_DOT9", KeyMask.SPACE);

    map("NEXT", KeyMask.FORWARD);
    map("PREVIOUS", KeyMask.BACKWARD);

    map("OK", KeyMask.DPAD_CENTER);
    map("UP", KeyMask.DPAD_UP);
    map("DOWN", KeyMask.DPAD_DOWN);
    map("LEFT", KeyMask.DPAD_LEFT);
    map("RIGHT", KeyMask.DPAD_RIGHT);
  }

  private ScanCode () {
  }
}
