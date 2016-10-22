package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MusicPrevious extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MEDIA_PREVIOUS;
  }

  public MusicPrevious (Endpoint endpoint) {
    super(endpoint, false);
  }
}
