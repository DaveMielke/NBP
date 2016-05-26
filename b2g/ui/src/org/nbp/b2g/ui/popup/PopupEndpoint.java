package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;
import org.nbp.b2g.ui.actions.*;

import org.nbp.common.Timeout;

public class PopupEndpoint extends Endpoint {
  private Timeout timeout = new Timeout(ApplicationParameters.BRAILLE_POPUP_TIMEOUT, "popup-timeout") {
    @Override
    public void run () {
      Endpoints.setHostEndpoint();
    }
  };

  @Override
  protected boolean braille () {
    timeout.start();
    return super.braille();
  }

  @Override
  public void onBackground () {
    synchronized (this) {
      try {
        timeout.cancel();
      } finally {
        super.onBackground();
      }
    }
  }

  private PopupClickHandler clickHandler = null;

  private final int getIndex () {
    int index = 0;
    int offset = getLineStart();

    while (true) {
      offset = findPreviousNewline(offset);
      if (offset == -1) return index;
      index += 1;
    }
  }

  @Override
  public boolean handleClick () {
    synchronized (this) {
      if (clickHandler != null) {
        if (clickHandler.handleValue(getIndex())) {
          return true;
        }
      }
    }

    Endpoints.setHostEndpoint();
    return false;
  }

  public final void set (CharSequence text, PopupClickHandler handler) {
    synchronized (this) {
      clickHandler = handler;
      write(text);
    }
  }

  public PopupEndpoint () {
    super("popup");
  }
}
