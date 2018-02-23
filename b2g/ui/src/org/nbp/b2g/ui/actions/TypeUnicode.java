package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeUnicode extends InputAction {
  @Override
  protected final boolean performInputAction (Endpoint endpoint) {
    return Endpoints.setUnicodeEndpoint();
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeUnicode (Endpoint endpoint) {
    super(endpoint);
  }
}
