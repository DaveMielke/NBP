package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeleteNext extends DeleteAction {
  @Override
  protected final int getDeleteOffset () {
    return 0;
  }

  public DeleteNext (Endpoint endpoint) {
    super(endpoint);
  }
}
