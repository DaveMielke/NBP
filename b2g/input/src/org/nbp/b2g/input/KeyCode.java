package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public class KeyCode {
  private static final String LOG_TAG = InputService.class.getName();

  public final static int Dot7    = 769;
  public final static int Dot3    = 770;
  public final static int Dot2    = 771;
  public final static int Dot1    = 772;
  public final static int Dot4    = 773;
  public final static int Dot5    = 774;
  public final static int Dot6    = 775;
  public final static int Dot8    = 776;
  public final static int Space   = 777;

  public final static int Cursor1 = 778;
  public final static int Cursor2 = 798;

  public final static int Back    = 818;
  public final static int Forward = 819;

  public static int toKeyMask (int code) {
    switch (code) {
      case KeyCode.Dot1:    return KeyMask.Dot1;
      case KeyCode.Dot2:    return KeyMask.Dot2;
      case KeyCode.Dot3:    return KeyMask.Dot3;
      case KeyCode.Dot4:    return KeyMask.Dot4;
      case KeyCode.Dot5:    return KeyMask.Dot5;
      case KeyCode.Dot6:    return KeyMask.Dot6;
      case KeyCode.Dot7:    return KeyMask.Dot7;
      case KeyCode.Dot8:    return KeyMask.Dot8;
      case KeyCode.Space:   return KeyMask.Space;

      case KeyCode.Forward: return KeyMask.Forward;
      case KeyCode.Back:    return KeyMask.Back;

      case KeyEvent.KEYCODE_DPAD_CENTER: return KeyMask.Center;
      case KeyEvent.KEYCODE_DPAD_LEFT:   return KeyMask.Left;
      case KeyEvent.KEYCODE_DPAD_RIGHT:  return KeyMask.Right;
      case KeyEvent.KEYCODE_DPAD_UP:     return KeyMask.Up;
      case KeyEvent.KEYCODE_DPAD_DOWN:   return KeyMask.Down;
    }

    return 0;
  }
}
