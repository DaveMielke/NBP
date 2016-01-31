package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeUnicode extends InputAction {
  @Override
  public boolean performInputAction () {
    Endpoints.setUnicodeEndpoint();
    return true;
  }

  public TypeUnicode (Endpoint endpoint) {
    super(endpoint);
  }
}
