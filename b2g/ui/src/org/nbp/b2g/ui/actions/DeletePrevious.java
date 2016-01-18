package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeletePrevious extends DeleteAction {
  @Override
  protected final int getDeleteOffset () {
    return -1;
  }

  public DeletePrevious (Endpoint endpoint) {
    super(endpoint);
  }
}
