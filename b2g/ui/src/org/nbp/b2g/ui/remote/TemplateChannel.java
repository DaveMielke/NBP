package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class TemplateChannel extends Channel {
  public TemplateChannel () {
    super();
  }

  @Override
  protected final void runChannelThread () {
  }

  @Override
  protected final void initializeChannelThread () {
  }

  @Override
  protected final void stopChannelThread () {
  }

  @Override
  public final boolean send (byte b) {
    return false;
  }

  @Override
  public final boolean flush () {
    return false;
  }
}
