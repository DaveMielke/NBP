package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;
import org.nbp.b2g.ui.actions.*;

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

  private ValueHandler<Integer> enterKeyHandler = null;

  @Override
  public boolean handleKeyboardKey_enter () {
    try {
      synchronized (this) {
        if (enterKeyHandler != null) {
          int index = 0;
          int offset = getLineStart();

          while (true) {
            int previous = findPreviousNewline(offset);
            if (previous == -1) break;

            index += 1;
            offset = previous;
          }

          if (!enterKeyHandler.handleValue(index)) return false;
        }
      }
    } finally {
      if (!super.handleKeyboardKey_enter()) return false;
    }

    return true;
  }

  public final void set (CharSequence text, ValueHandler<Integer> handler) {
    synchronized (this) {
      enterKeyHandler = handler;
      write(text);
    }
  }

  public PopupEndpoint () {
    super("popup");
  }
}
