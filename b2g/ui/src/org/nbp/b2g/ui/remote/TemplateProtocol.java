package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class TemplateProtocol extends Protocol {
  @Override
  public final void resetInput () {
  }

  @Override
  public final boolean handleTimeout () {
    return true;
  }

  @Override
  public final boolean handleInput (byte b) {
    return true;
  }

  @Override
  public final int handleNavigationKeyEvent (int keyMask, boolean press) {
    return keyMask;
  }

  @Override
  public final boolean handleCursorKeyEvent (int keyNumber, boolean press) {
    return false;
  }

  @Override
  public final void resetKeys () {
  }

  public TemplateProtocol () {
    super();
  }
}
