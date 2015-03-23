package org.nbp.b2g.input;

public class ArrowAction extends ScanCodeAction {
  protected int getArrowKeyCode () {
    return NULL_KEY_CODE;
  }

  @Override
  protected int getKeyCode () {
    if (ApplicationParameters.CHORDS_SEND_ARROW_KEYS && isChord()) return NULL_KEY_CODE;
    return getArrowKeyCode();
  }

  public ArrowAction () {
    super();
  }
}
