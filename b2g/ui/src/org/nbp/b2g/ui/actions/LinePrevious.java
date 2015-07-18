package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LinePrevious extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int start = endpoint.getLineStart();
      if (start == 0) return false;

      endpoint.setLine(start-1);
      endpoint.setLineIndent(0);
    }

    return endpoint.write();
  }

  public LinePrevious (Endpoint endpoint) {
    super(endpoint, false);
  }
}
