package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeleteNextCharacter extends DeleteAction {
  @Override
  protected final int getDeleteOffset () {
    return 0;
  }

  public DeleteNextCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
