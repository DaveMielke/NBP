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

  public final static int Dot1    = 769; //  getKeyCode("KEYCODE_BRL_DOT1");
  public final static int Dot2    = 770; //  getKeyCode("KEYCODE_BRL_DOT2");
  public final static int Dot3    = 771; //  getKeyCode("KEYCODE_BRL_DOT3");
  public final static int Dot4    = 772; //  getKeyCode("KEYCODE_BRL_DOT4");
  public final static int Dot5    = 773; //  getKeyCode("KEYCODE_BRL_DOT5");
  public final static int Dot6    = 774; //  getKeyCode("KEYCODE_BRL_DOT6");
  public final static int Dot7    = 775; //  getKeyCode("KEYCODE_BRL_DOT7");
  public final static int Dot8    = 776; //  getKeyCode("KEYCODE_BRL_DOT8");
  public final static int Dot9    = 777; //  getKeyCode("KEYCODE_BRL_DOT9");

  public final static int Cursor1 = 778; //  getKeyCode("KEYCODE_BRL_CURSOR0");
  public final static int Cursor2 = 798; //  getKeyCode("KEYCODE_BRL_CURSOR20");

  public final static int Back    = 818; //  getKeyCode("KEYCODE_BRL_BACK");
  public final static int Forward = 819; //  getKeyCode("KEYCODE_BRL_FORWARD");

  public static int toKeyBit (int code) {
    if (code == KeyCode.Dot4)    return KeyBit.Dot1;
    if (code == KeyCode.Dot3)    return KeyBit.Dot2;
    if (code == KeyCode.Dot2)    return KeyBit.Dot3;
    if (code == KeyCode.Dot5)    return KeyBit.Dot4;
    if (code == KeyCode.Dot6)    return KeyBit.Dot5;
    if (code == KeyCode.Dot7)    return KeyBit.Dot6;
    if (code == KeyCode.Dot1)    return KeyBit.Dot7;
    if (code == KeyCode.Dot8)    return KeyBit.Dot8;
    if (code == KeyCode.Dot9)    return KeyBit.Space;

    if (code == KeyCode.Forward) return KeyBit.Forward;
    if (code == KeyCode.Back)    return KeyBit.Back;

    return 0;
  }
}
