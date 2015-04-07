package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class Search extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "SEARCH";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_SEARCH;
  }

  public Search (Endpoint endpoint) {
    super(endpoint, false);
  }
}
