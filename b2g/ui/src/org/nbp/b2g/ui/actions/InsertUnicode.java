package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InsertUnicode extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setUnicodeEndpoint();
    return true;
  }

  public InsertUnicode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
