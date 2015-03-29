package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class VolumeUp extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "VOLUMEUP";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_VOLUME_UP;
  }

  public VolumeUp () {
    super();
  }
}
