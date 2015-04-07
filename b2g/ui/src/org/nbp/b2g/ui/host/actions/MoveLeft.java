package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveLeft extends MoveBackward {
  private boolean panLeft (Endpoint endpoint) {
    int indent = endpoint.getLineIndent();

    if (indent == 0) {
      int start = endpoint.getLineStart();
      if (start == 0) return false;

      endpoint.setLine(start-1);
      indent = endpoint.getLineLength() + 1;
    } else {
      int length = endpoint.getLineLength();
      if (indent > length) indent = length;
    }

    if ((indent -= BrailleDevice.size()) < 0) indent = 0;
    endpoint.setLineIndent(indent);
    return endpoint.write();
  }

  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (panLeft(endpoint)) return true;
      if (endpoint.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveLeft (Endpoint endpoint) {
    super(endpoint);
  }
}
