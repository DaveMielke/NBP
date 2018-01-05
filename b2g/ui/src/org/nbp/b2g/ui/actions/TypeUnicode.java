package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeUnicode extends InputAction {
  @Override
  protected final boolean performInputAction (Endpoint endpoint) {
    return Endpoints.setUnicodeEndpoint();
  }

  public TypeUnicode (Endpoint endpoint) {
    super(endpoint);
  }
}
