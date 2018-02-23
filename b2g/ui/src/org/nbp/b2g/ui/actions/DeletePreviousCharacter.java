package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeletePreviousCharacter extends DeleteAction {
  @Override
  protected final int getDeleteOffset () {
    return -1;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public DeletePreviousCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
