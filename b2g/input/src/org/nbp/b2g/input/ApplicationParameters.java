package org.nbp.b2g.input;

public abstract class ApplicationParameters {
  public static volatile boolean LOG_KEYBOARD_EVENTS = false;
  public static volatile boolean CHORDS_SEND_ARROWS = true;

  private ApplicationParameters () {
  }
}
