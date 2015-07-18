package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LineNext extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int end = endpoint.getLineStart() + endpoint.getLineLength();
      if (end == endpoint.getTextLength()) return false;

      endpoint.setLine(end+1);
      endpoint.setLineIndent(0);
    }

    return endpoint.write();
  }

  public LineNext (Endpoint endpoint) {
    super(endpoint, false);
  }
}
