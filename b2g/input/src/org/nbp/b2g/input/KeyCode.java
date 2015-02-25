package org.nbp.b2g.input;

public class KeyCode {
  public final static int Up      = 0X103;
  public final static int Left    = 0X105;
  public final static int Right   = 0X106;
  public final static int Down    = 0X108;
  public final static int Center  = 0X352;

  public final static int Forward = 0X197;
  public final static int Back    = 0X19C;

  public final static int Dot1    = 0X1F1;
  public final static int Dot2    = 0X1F2;
  public final static int Dot3    = 0X1F3;
  public final static int Dot4    = 0X1F4;
  public final static int Dot5    = 0X1F5;
  public final static int Dot6    = 0X1F6;
  public final static int Dot7    = 0X1F7;
  public final static int Dot8    = 0X1F8;
  public final static int Dot9    = 0X1F9;

  public final static int Cursor1 = 0X2D0;
  public final static int Cursor2 = 0X2E4;

  public static int toKeyBit (int code) {
    switch (code) {
      case KeyCode.Dot4:    return KeyBit.Dot1;
      case KeyCode.Dot3:    return KeyBit.Dot2;
      case KeyCode.Dot2:    return KeyBit.Dot3;
      case KeyCode.Dot5:    return KeyBit.Dot4;
      case KeyCode.Dot6:    return KeyBit.Dot5;
      case KeyCode.Dot7:    return KeyBit.Dot6;
      case KeyCode.Dot1:    return KeyBit.Dot7;
      case KeyCode.Dot8:    return KeyBit.Dot8;
      case KeyCode.Dot9:    return KeyBit.Space;

      case KeyCode.Forward: return KeyBit.Forward;
      case KeyCode.Back:    return KeyBit.Back;

      case KeyCode.Left:    return KeyBit.Left;
      case KeyCode.Right:   return KeyBit.Right;
      case KeyCode.Up:      return KeyBit.Up;
      case KeyCode.Down:    return KeyBit.Down;
      case KeyCode.Center:  return KeyBit.Center;
    }

    return 0;
  }
}
