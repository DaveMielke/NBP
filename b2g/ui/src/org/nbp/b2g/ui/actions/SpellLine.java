package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpellLine extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      CharSequence text = endpoint.getLineText();
      return ApplicationUtilities.spell(text);
    }
  }

  public SpellLine (Endpoint endpoint) {
    super(endpoint, false);
  }
}
