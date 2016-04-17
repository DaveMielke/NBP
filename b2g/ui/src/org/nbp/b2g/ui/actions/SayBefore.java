package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayBefore extends SayAction {
  @Override
  protected final CharSequence getText (Endpoint endpoint) {
    return endpoint.getText().subSequence(0, endpoint.getBrailleStart());
  }

  public SayBefore (Endpoint endpoint) {
    super(endpoint);
  }
}
