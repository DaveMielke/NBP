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
  private int headerLines = 0;

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
      if (clickHandler == null) return false;

      int index = getIndex() - headerLines;
      if (index < 0) return false;

      try {
        return clickHandler.handleValue(index);
      } finally {
        if (!handleKeyboardKey_enter()) return false;
      }
    }
  }

  public final void set (CharSequence text, int first, PopupClickHandler handler) {
    synchronized (this) {
      clickHandler = handler;
      headerLines = first;
      write(text);
    }
  }

  public PopupEndpoint () {
    super(true);
    addKeyBindings("popup");
  }
}
