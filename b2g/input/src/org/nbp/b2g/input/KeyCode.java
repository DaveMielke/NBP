package org.nbp.b2g.input;

import android.util.Log;

import android.view.KeyEvent;

public class KeyCode {
  private static final String LOG_TAG = InputService.class.getName();

  private static int getKeyCode (String name) {
    int code = KeyEvent.keyCodeFromString(name);
    Log.d(LOG_TAG, "key code for " + name + ": " + code);
    return code;
  }

  public final static int Dot7    = 769; //  getKeyCode("KEYCODE_BRL_DOT1");
  public final static int Dot3    = 770; //  getKeyCode("KEYCODE_BRL_DOT2");
  public final static int Dot2    = 771; //  getKeyCode("KEYCODE_BRL_DOT3");
  public final static int Dot1    = 772; //  getKeyCode("KEYCODE_BRL_DOT4");
  public final static int Dot4    = 773; //  getKeyCode("KEYCODE_BRL_DOT5");
  public final static int Dot5    = 774; //  getKeyCode("KEYCODE_BRL_DOT6");
  public final static int Dot6    = 775; //  getKeyCode("KEYCODE_BRL_DOT7");
  public final static int Dot8    = 776; //  getKeyCode("KEYCODE_BRL_DOT8");
  public final static int Space   = 777; //  getKeyCode("KEYCODE_BRL_DOT9");

  public final static int Cursor1 = 778; //  getKeyCode("KEYCODE_BRL_CURSOR0");
  public final static int Cursor2 = 798; //  getKeyCode("KEYCODE_BRL_CURSOR20");

  public final static int Back    = 818; //  getKeyCode("KEYCODE_BRL_BACK");
  public final static int Forward = 819; //  getKeyCode("KEYCODE_BRL_FORWARD");

  public static int toKeyMask (int code) {
    if (code == KeyCode.Dot1)    return KeyMask.Dot1;
    if (code == KeyCode.Dot2)    return KeyMask.Dot2;
    if (code == KeyCode.Dot3)    return KeyMask.Dot3;
    if (code == KeyCode.Dot4)    return KeyMask.Dot4;
    if (code == KeyCode.Dot5)    return KeyMask.Dot5;
    if (code == KeyCode.Dot6)    return KeyMask.Dot6;
    if (code == KeyCode.Dot7)    return KeyMask.Dot7;
    if (code == KeyCode.Dot8)    return KeyMask.Dot8;
    if (code == KeyCode.Space)   return KeyMask.Space;

    if (code == KeyCode.Forward) return KeyMask.Forward;
    if (code == KeyCode.Back)    return KeyMask.Back;

    return 0;
  }
}
