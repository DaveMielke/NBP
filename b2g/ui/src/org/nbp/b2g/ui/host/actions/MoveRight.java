package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveRight extends MoveForward {
  private boolean panRight (Endpoint endpoint) {
    int indent = endpoint.getLineIndent() + BrailleDevice.size();
    int length = endpoint.getLineLength();

    if (indent > length) {
      int offset = endpoint.getLineStart() + length + 1;
      if (offset > endpoint.getTextLength()) return false;

      endpoint.setLine(offset);
      indent = 0;
    }

    endpoint.setLineIndent(indent);
    return endpoint.write();
  }

  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (panRight(endpoint)) return true;
      if (endpoint.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveRight (Endpoint endpoint) {
    super(endpoint);
  }
}
