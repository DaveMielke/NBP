package org.nbp.b2g.ui.find;
import org.nbp.b2g.ui.*;

public abstract class FindAction extends Action {
  protected FindEndpoint getFindEndpoint () {
    return (FindEndpoint)getEndpoint();
  }

  protected FindAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
