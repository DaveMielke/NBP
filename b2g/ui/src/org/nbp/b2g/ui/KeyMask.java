package org.nbp.b2g.ui;

public class KeyMask {
  public final static int DOT_1       = 0X000001;
  public final static int DOT_2       = 0X000002;
  public final static int DOT_3       = 0X000004;
  public final static int DOT_4       = 0X000008;
  public final static int DOT_5       = 0X000010;
  public final static int DOT_6       = 0X000020;
  public final static int DOT_7       = 0X000040;
  public final static int DOT_8       = 0X000080;
  public final static int SPACE       = 0X000100;
  public final static int FORWARD     = 0X000200;
  public final static int BACKWARD    = 0X000400;
  public final static int DPAD_CENTER = 0X000800;
  public final static int DPAD_LEFT   = 0X001000;
  public final static int DPAD_RIGHT  = 0X002000;
  public final static int DPAD_UP     = 0X004000;
  public final static int DPAD_DOWN   = 0X008000;
  public final static int VOLUME_DOWN = 0X010000;
  public final static int VOLUME_UP   = 0X020000;
  public final static int POWER_ON    = 0X040000;
  public final static int POWER_OFF   = 0X080000;
  public final static int CURSOR      = 0X100000;
  public final static int LONG_PRESS  = 0X200000;

  public final static int DOTS_ALL = (DOT_1 | DOT_2 | DOT_3 | DOT_4 | DOT_5 | DOT_6 | DOT_7 | DOT_8);
}
