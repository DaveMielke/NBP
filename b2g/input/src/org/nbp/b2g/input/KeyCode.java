package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public class KeyCode {
  private static final String LOG_TAG = InputService.class.getName();

  public final static int Dot7     = 769;
  public final static int Dot3     = 770;
  public final static int Dot2     = 771;
  public final static int Dot1     = 772;
  public final static int Dot4     = 773;
  public final static int Dot5     = 774;
  public final static int Dot6     = 775;
  public final static int Dot8     = 776;
  public final static int Space    = 777;

  public final static int Cursor0  = 778;
  public final static int Cursor1  = 779;
  public final static int Cursor2  = 780;
  public final static int Cursor3  = 781;
  public final static int Cursor4  = 782;
  public final static int Cursor5  = 783;
  public final static int Cursor6  = 784;
  public final static int Cursor7  = 785;
  public final static int Cursor8  = 786;
  public final static int Cursor9  = 787;
  public final static int Cursor10 = 788;
  public final static int Cursor11 = 789;
  public final static int Cursor12 = 790;
  public final static int Cursor13 = 791;
  public final static int Cursor14 = 792;
  public final static int Cursor15 = 793;
  public final static int Cursor16 = 794;
  public final static int Cursor17 = 795;
  public final static int Cursor18 = 796;
  public final static int Cursor19 = 797;

  public final static int Cursor20 = 798;
  public final static int Cursor21 = 799;
  public final static int Cursor22 = 800;
  public final static int Cursor23 = 801;
  public final static int Cursor24 = 802;
  public final static int Cursor25 = 803;
  public final static int Cursor26 = 804;
  public final static int Cursor27 = 805;
  public final static int Cursor28 = 806;
  public final static int Cursor29 = 807;
  public final static int Cursor30 = 808;
  public final static int Cursor31 = 809;
  public final static int Cursor32 = 810;
  public final static int Cursor33 = 811;
  public final static int Cursor34 = 812;
  public final static int Cursor35 = 813;
  public final static int Cursor36 = 814;
  public final static int Cursor37 = 815;
  public final static int Cursor38 = 816;
  public final static int Cursor39 = 817;

  public final static int Backward = 818;
  public final static int Forward  = 819;

  public static int toKeyMask (int code) {
    switch (code) {
      case KeyCode.Dot1:     return KeyMask.Dot1;
      case KeyCode.Dot2:     return KeyMask.Dot2;
      case KeyCode.Dot3:     return KeyMask.Dot3;
      case KeyCode.Dot4:     return KeyMask.Dot4;
      case KeyCode.Dot5:     return KeyMask.Dot5;
      case KeyCode.Dot6:     return KeyMask.Dot6;
      case KeyCode.Dot7:     return KeyMask.Dot7;
      case KeyCode.Dot8:     return KeyMask.Dot8;
      case KeyCode.Space:    return KeyMask.Space;

      case KeyCode.Forward:  return KeyMask.Forward;
      case KeyCode.Backward: return KeyMask.Backward;

      case KeyEvent.KEYCODE_DPAD_CENTER: return KeyMask.Center;
      case KeyEvent.KEYCODE_DPAD_LEFT:   return KeyMask.Left;
      case KeyEvent.KEYCODE_DPAD_RIGHT:  return KeyMask.Right;
      case KeyEvent.KEYCODE_DPAD_UP:     return KeyMask.Up;
      case KeyEvent.KEYCODE_DPAD_DOWN:   return KeyMask.Down;
    }

    return 0;
  }
}
