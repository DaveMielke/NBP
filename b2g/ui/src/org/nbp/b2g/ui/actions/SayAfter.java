package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayAfter extends SayAction {
  @Override
  protected final CharSequence getText (Endpoint endpoint) {
    CharSequence text = endpoint.getText();
    return text.subSequence(endpoint.getBrailleStart(), text.length());
  }

  public SayAfter (Endpoint endpoint) {
    super(endpoint);
  }
}
