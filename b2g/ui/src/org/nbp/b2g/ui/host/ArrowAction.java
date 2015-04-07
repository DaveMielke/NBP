package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

public abstract class ArrowAction extends InputAction {
  protected boolean performArrowEditAction () {
    return false;
  }

  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        return performArrowEditAction();
      }
    }

    return super.performAction();
  }

  protected int getArrowKeyCode () {
    return NULL_KEY_CODE;
  }

  @Override
  protected int getKeyCode () {
    if (ApplicationParameters.CHORDS_SEND_ARROW_KEYS && isChord()) return NULL_KEY_CODE;
    return getArrowKeyCode();
  }

  protected ArrowAction (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
