package org.nbp.b2g.input;

import android.view.KeyEvent;

public class VolumeDownAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "VOLUMEDOWN";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_VOLUME_DOWN;
  }

  public VolumeDownAction () {
    super();
  }
}
