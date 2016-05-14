package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

public abstract class Component {
  protected final static int BYTE_MASK = 0XFF;
  protected final DisplayEndpoint displayEndpoint;

  protected Component (DisplayEndpoint endpoint) {
    displayEndpoint = endpoint;
  }

  protected final boolean write (String message) {
    return displayEndpoint.write(message);
  }
}
