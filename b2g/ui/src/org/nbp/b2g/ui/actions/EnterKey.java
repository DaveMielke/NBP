package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class EnterKey extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleEnterKey();
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint, false);
  }
}
