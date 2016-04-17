package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayAll extends SayAction {
  @Override
  protected final CharSequence getText (Endpoint endpoint) {
    return endpoint.getText();
  }

  public SayAll (Endpoint endpoint) {
    super(endpoint);
  }
}
