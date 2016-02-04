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

  private ValueHandler<Integer> clickHandler = null;

  @Override
  public boolean handleClick () {
    try {
      synchronized (this) {
        if (clickHandler == null) return false;

        int index = 0;
        int offset = getLineStart();

        while (true) {
          int previous = findPreviousNewline(offset);
          if (previous == -1) break;

          index += 1;
          offset = previous;
        }

        return clickHandler.handleValue(index);
      }
    } finally {
      Endpoints.setHostEndpoint();
    }
  }

  public final void set (CharSequence text, ValueHandler<Integer> handler) {
    synchronized (this) {
      clickHandler = handler;
      write(text);
    }
  }

  public PopupEndpoint () {
    super("popup");
  }
}
