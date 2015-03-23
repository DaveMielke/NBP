package org.nbp.b2g.input;

import android.view.KeyEvent;

public class SearchAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "COMPOSE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_SEARCH;
  }

  public SearchAction () {
    super();
  }
}
