package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class AnswerCall extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setHostEndpoint();
    return InputService.injectKey(KeyEvent.KEYCODE_HEADSETHOOK);
  }

  public AnswerCall (Endpoint endpoint) {
    super(endpoint, false);
  }
}
