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
  public final boolean handleNavigationKeyEvent (int key, boolean press) {
    return false;
  }

  @Override
  public final boolean handleCursorKeyEvent (int key, boolean press) {
    return false;
  }

  @Override
  public final void resetKeys () {
  }

  public TemplateProtocol () {
    super();
  }
}
