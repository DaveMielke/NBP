package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class ScanCode {
  private final static String LOG_TAG = ScanCode.class.getName();

  public final static int DPAD_CENTER = 0X160;

  public final static int FORWARD     = 0X197;
  public final static int BACKWARD    = 0X19C;

  public final static int DOT_7       = 0X1F1;
  public final static int DOT_3       = 0X1F2;
  public final static int DOT_2       = 0X1F3;
  public final static int DOT_1       = 0X1F4;
  public final static int DOT_4       = 0X1F5;
  public final static int DOT_5       = 0X1F6;
  public final static int DOT_6       = 0X1F7;
  public final static int DOT_8       = 0X1F8;
  public final static int SPACE       = 0X1F9;
  public final static int WAKEUP      = 0X1FB;

  public final static int CURSOR_0    = 0X2D0;
  public final static int CURSOR_1    = 0X2D1;
  public final static int CURSOR_2    = 0X2D2;
  public final static int CURSOR_3    = 0X2D3;
  public final static int CURSOR_4    = 0X2D4;
  public final static int CURSOR_5    = 0X2D5;
  public final static int CURSOR_6    = 0X2D6;
  public final static int CURSOR_7    = 0X2D7;
  public final static int CURSOR_8    = 0X2D8;
  public final static int CURSOR_9    = 0X2D9;
  public final static int CURSOR_10   = 0X2DA;
  public final static int CURSOR_11   = 0X2DB;
  public final static int CURSOR_12   = 0X2DC;
  public final static int CURSOR_13   = 0X2DD;
  public final static int CURSOR_14   = 0X2DE;
  public final static int CURSOR_15   = 0X2DF;
  public final static int CURSOR_16   = 0X2E0;
  public final static int CURSOR_17   = 0X2E1;
  public final static int CURSOR_18   = 0X2E2;
  public final static int CURSOR_19   = 0X2E3;

  public final static int CURSOR_20   = 0X2E4;
  public final static int CURSOR_21   = 0X2E5;
  public final static int CURSOR_22   = 0X2E6;
  public final static int CURSOR_23   = 0X2E7;
  public final static int CURSOR_24   = 0X2E8;
  public final static int CURSOR_25   = 0X2E9;
  public final static int CURSOR_26   = 0X2EA;
  public final static int CURSOR_27   = 0X2EB;
  public final static int CURSOR_28   = 0X2EC;
  public final static int CURSOR_29   = 0X2ED;
  public final static int CURSOR_30   = 0X2EE;
  public final static int CURSOR_31   = 0X2EF;
  public final static int CURSOR_32   = 0X2F0;
  public final static int CURSOR_33   = 0X2F1;
  public final static int CURSOR_34   = 0X2F2;
  public final static int CURSOR_35   = 0X2F3;
  public final static int CURSOR_36   = 0X2F4;
  public final static int CURSOR_37   = 0X2F5;
  public final static int CURSOR_38   = 0X2F6;
  public final static int CURSOR_39   = 0X2F7;

  public final static int DOTS        = 0X300;
  public final static int CHORD       = 0X400;

  private static Map<Integer, Integer> scanCodeMap = new HashMap<Integer, Integer>();

  public static int toKeyMask (int code) {
    switch (code) {
      case ScanCode.DOT_1:    return KeyMask.DOT_1;
      case ScanCode.DOT_2:    return KeyMask.DOT_2;
      case ScanCode.DOT_3:    return KeyMask.DOT_3;
      case ScanCode.DOT_4:    return KeyMask.DOT_4;
      case ScanCode.DOT_5:    return KeyMask.DOT_5;
      case ScanCode.DOT_6:    return KeyMask.DOT_6;
      case ScanCode.DOT_7:    return KeyMask.DOT_7;
      case ScanCode.DOT_8:    return KeyMask.DOT_8;
      case ScanCode.SPACE:    return KeyMask.SPACE;

      case ScanCode.FORWARD:  return KeyMask.FORWARD;
      case ScanCode.BACKWARD: return KeyMask.BACKWARD;
      case ScanCode.DPAD_CENTER: return KeyMask.DPAD_CENTER;
    }

    Integer mask = scanCodeMap.get(code);
    if (mask != null) return mask;

    return 0;
  }

  private ScanCode () {
  }

  private static void map (String name, int mask) {
    int code = Keyboard.getScanCodeValue(name);

    if (code != Keyboard.NULL_SCAN_CODE) {
      scanCodeMap.put(code, mask);
    }
  }

  static {
    map("UP", KeyMask.DPAD_UP);
    map("DOWN", KeyMask.DPAD_DOWN);
    map("LEFT", KeyMask.DPAD_LEFT);
    map("RIGHT", KeyMask.DPAD_RIGHT);

    map("VOLUMEDOWN", KeyMask.VOLUME_DOWN);
    map("VOLUMEUP", KeyMask.VOLUME_UP);
  }
}
