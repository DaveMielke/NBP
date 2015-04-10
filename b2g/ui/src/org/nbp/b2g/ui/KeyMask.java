package org.nbp.b2g.ui;

public class KeyMask {
  public final static int DOT_1       = 0X00001;
  public final static int DOT_2       = 0X00002;
  public final static int DOT_3       = 0X00004;
  public final static int DOT_4       = 0X00008;
  public final static int DOT_5       = 0X00010;
  public final static int DOT_6       = 0X00020;
  public final static int DOT_7       = 0X00040;
  public final static int DOT_8       = 0X00080;
  public final static int SPACE       = 0X00100;
  public final static int FORWARD     = 0X00200;
  public final static int BACKWARD    = 0X00400;
  public final static int DPAD_CENTER = 0X00800;
  public final static int DPAD_LEFT   = 0X01000;
  public final static int DPAD_RIGHT  = 0X02000;
  public final static int DPAD_UP     = 0X04000;
  public final static int DPAD_DOWN   = 0X08000;
  public final static int VOLUME_DOWN = 0X10000;
  public final static int VOLUME_UP   = 0X20000;
  public final static int CURSOR      = 0X40000;
  public final static int LONG_PRESS  = 0X80000;

  public final static int DOTS_ALL = (DOT_1 | DOT_2 | DOT_3 | DOT_4 | DOT_5 | DOT_6 | DOT_7 | DOT_8);
}
