package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.KeyEvent;

public class VolumeDown extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "VOLUMEDOWN";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_VOLUME_DOWN;
  }

  public VolumeDown () {
    super();
  }
}
