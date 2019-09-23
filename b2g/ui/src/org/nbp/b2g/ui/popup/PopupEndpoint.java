package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;
import org.nbp.b2g.ui.actions.*;

import org.nbp.common.Timeout;

public class PopupEndpoint extends Endpoint {
  private Timeout timeout = new Timeout(ApplicationParameters.BRAILLE_POPUP_TIMEOUT, "popup-timeout") {
    @Override
    public void run () {
      Endpoints.setPreviousEndpoint();
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

  private int headerLines = 0;
  private PopupClickHandler clickHandler = null;
  private KeyBindingMap dotsBindings = null;

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
        return clickHandler.handleClick(index);
      } finally {
        if (Endpoints.getCurrentEndpoint() == this) {
          if (!Endpoints.setPreviousEndpoint()) {
            return false;
          }
        }
      }
    }
  }

  @Override
  public boolean handleDotKeys (byte dots) {
    if (dotsBindings == null) return false;

    Action action = dotsBindings.get(KeySet.fromDots(dots));
    if (action == null) return false;

    Endpoints.setPreviousEndpoint();
    return KeyEvents.performAction(action);
  }

  public final PopupEndpoint resetPopupEndpoint (CharSequence text) {
    write(text);
    headerLines = 0;
    clickHandler = null;
    dotsBindings = null;
    return this;
  }

  public final PopupEndpoint setHeaderLines (int lines) {
    headerLines = lines;
    return this;
  }

  public final PopupEndpoint setClickHandler (PopupClickHandler handler) {
    clickHandler = handler;
    return this;
  }

  public final PopupEndpoint setDotsBindings (KeyBindingMap bindings) {
    dotsBindings = bindings;
    return this;
  }

  public PopupEndpoint () {
    super(true);
    addKeyBindings("popup");
  }
}
