package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

public abstract class ArrowAction extends InputAction {
  protected boolean performArrowEditAction (Endpoint endpoint) {
    return false;
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        return performArrowEditAction(endpoint);
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

  protected ArrowAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
