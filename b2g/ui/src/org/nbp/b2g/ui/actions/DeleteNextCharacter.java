package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeleteNextCharacter extends DeleteAction {
  @Override
  protected final int getDeleteOffset () {
    return 0;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public DeleteNextCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
