package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public class KeyCode {
  private final static String LOG_TAG = KeyCode.class.getName();

  public final static int DOT_7     = 769;
  public final static int DOT_3     = 770;
  public final static int DOT_2     = 771;
  public final static int DOT_1     = 772;
  public final static int DOT_4     = 773;
  public final static int DOT_5     = 774;
  public final static int DOT_6     = 775;
  public final static int DOT_8     = 776;
  public final static int SPACE     = 777;

  public final static int CURSOR_0  = 778;
  public final static int CURSOR_1  = 779;
  public final static int CURSOR_2  = 780;
  public final static int CURSOR_3  = 781;
  public final static int CURSOR_4  = 782;
  public final static int CURSOR_5  = 783;
  public final static int CURSOR_6  = 784;
  public final static int CURSOR_7  = 785;
  public final static int CURSOR_8  = 786;
  public final static int CURSOR_9  = 787;
  public final static int CURSOR_10 = 788;
  public final static int CURSOR_11 = 789;
  public final static int CURSOR_12 = 790;
  public final static int CURSOR_13 = 791;
  public final static int CURSOR_14 = 792;
  public final static int CURSOR_15 = 793;
  public final static int CURSOR_16 = 794;
  public final static int CURSOR_17 = 795;
  public final static int CURSOR_18 = 796;
  public final static int CURSOR_19 = 797;

  public final static int CURSOR_20 = 798;
  public final static int CURSOR_21 = 799;
  public final static int CURSOR_22 = 800;
  public final static int CURSOR_23 = 801;
  public final static int CURSOR_24 = 802;
  public final static int CURSOR_25 = 803;
  public final static int CURSOR_26 = 804;
  public final static int CURSOR_27 = 805;
  public final static int CURSOR_28 = 806;
  public final static int CURSOR_29 = 807;
  public final static int CURSOR_30 = 808;
  public final static int CURSOR_31 = 809;
  public final static int CURSOR_32 = 810;
  public final static int CURSOR_33 = 811;
  public final static int CURSOR_34 = 812;
  public final static int CURSOR_35 = 813;
  public final static int CURSOR_36 = 814;
  public final static int CURSOR_37 = 815;
  public final static int CURSOR_38 = 816;
  public final static int CURSOR_39 = 817;

  public final static int BACKWARD  = 818;
  public final static int FORWARD   = 819;

  public static int toKeyMask (int code) {
    switch (code) {
      case KeyCode.DOT_1:    return KeyMask.DOT_1;
      case KeyCode.DOT_2:    return KeyMask.DOT_2;
      case KeyCode.DOT_3:    return KeyMask.DOT_3;
      case KeyCode.DOT_4:    return KeyMask.DOT_4;
      case KeyCode.DOT_5:    return KeyMask.DOT_5;
      case KeyCode.DOT_6:    return KeyMask.DOT_6;
      case KeyCode.DOT_7:    return KeyMask.DOT_7;
      case KeyCode.DOT_8:    return KeyMask.DOT_8;
      case KeyCode.SPACE:    return KeyMask.SPACE;

      case KeyCode.FORWARD:  return KeyMask.FORWARD;
      case KeyCode.BACKWARD: return KeyMask.BACKWARD;

      case KeyEvent.KEYCODE_DPAD_CENTER: return KeyMask.DPAD_CENTER;
      case KeyEvent.KEYCODE_DPAD_LEFT:   return KeyMask.DPAD_LEFT;
      case KeyEvent.KEYCODE_DPAD_RIGHT:  return KeyMask.DPAD_RIGHT;
      case KeyEvent.KEYCODE_DPAD_UP:     return KeyMask.DPAD_UP;
      case KeyEvent.KEYCODE_DPAD_DOWN:   return KeyMask.DPAD_DOWN;
    }

    return 0;
  }
}
