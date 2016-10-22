package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MusicPlayPause extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
  }

  public MusicPlayPause (Endpoint endpoint) {
    super(endpoint, false);
  }
}
