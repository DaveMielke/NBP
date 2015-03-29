package org.nbp.b2g.ui;

public abstract class SystemAction extends GlobalAction {
  protected int getSystemKeyCode () {
    return NULL_KEY_CODE;
  }

  @Override
  protected int getKeyCode () {
    if (ApplicationParameters.CHORDS_SEND_SYSTEM_KEYS && isChord()) return NULL_KEY_CODE;
    return getSystemKeyCode();
  }

  protected SystemAction () {
    super();
  }
}
