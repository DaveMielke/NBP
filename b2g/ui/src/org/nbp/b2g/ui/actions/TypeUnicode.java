package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeUnicode extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setUnicodeEndpoint();
    return true;
  }

  public TypeUnicode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
