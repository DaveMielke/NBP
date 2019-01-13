package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class ScanCode {
  private final static String LOG_TAG = ScanCode.class.getName();

  private ScanCode () {
  }

  public final static int POWER;
  public final static int WAKEUP;

  public final static int CURSOR_FIRST;
  public final static int CURSOR_LAST;

  public final static int DOTS        = 0X300;
  public final static int CHORD       = 0X400;

  private final static Map<Integer, Integer> scanCodeMap =
               new HashMap<Integer, Integer>();

  public static Integer toKey (int code) {
    {
      Integer key = scanCodeMap.get(code);
      if (key != null) return key;
    }

    return null;
  }

  private static int map (String name, Integer key) {
    int code = Keyboard.getScanCodeValue(name);

    if (code != Keyboard.NULL_SCAN_CODE) {
      scanCodeMap.put(code, key);
    }

    return code;
  }

  private static int map (String name) {
    return map(name, null);
  }

  static {
    POWER = map("POWER");
    WAKEUP = map("B2G_WAKEUP");

    CURSOR_FIRST = map("B2G_CURSOR_0");
    CURSOR_LAST = map("B2G_CURSOR_39");

    map("VOLUMEDOWN", KeySet.VOLUME_DOWN);
    map("VOLUMEUP", KeySet.VOLUME_UP);

    map("BRL_DOT1", KeySet.DOT_7);
    map("BRL_DOT2", KeySet.DOT_3);
    map("BRL_DOT3", KeySet.DOT_2);
    map("BRL_DOT4", KeySet.DOT_1);
    map("BRL_DOT5", KeySet.DOT_4);
    map("BRL_DOT6", KeySet.DOT_5);
    map("BRL_DOT7", KeySet.DOT_6);
    map("BRL_DOT8", KeySet.DOT_8);
    map("BRL_DOT9", KeySet.SPACE);

    map("NEXT", KeySet.PAN_FORWARD);
    map("PREVIOUS", KeySet.PAN_BACKWARD);

    map("UP", KeySet.PAD_UP);
    map("DOWN", KeySet.PAD_DOWN);
    map("LEFT", KeySet.PAD_LEFT);
    map("RIGHT", KeySet.PAD_RIGHT);
    map("OK", KeySet.PAD_CENTER);
  }
}
