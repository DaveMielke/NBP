package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class Assist extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ASSIST;
  }

  public Assist () {
    super(false);
  }
}
