package org.nbp.b2g.input;

import android.view.KeyEvent;

public class VolumeUpAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "VOLUMEUP";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_VOLUME_UP;
  }

  public VolumeUpAction () {
    super();
  }
}
