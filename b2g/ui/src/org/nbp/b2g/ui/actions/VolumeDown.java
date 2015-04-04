package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

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
    super(false);
  }
}
