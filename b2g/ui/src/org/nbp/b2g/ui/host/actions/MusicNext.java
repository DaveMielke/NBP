package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MusicNext extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MEDIA_NEXT;
  }

  public MusicNext (Endpoint endpoint) {
    super(endpoint, false);
  }
}
