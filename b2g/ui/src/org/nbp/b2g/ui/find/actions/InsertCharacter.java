package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

public class InsertCharacter extends InsertCharacterAction {
  @Override
  public boolean insertCharacter (char character) {
    FindEndpoint endpoint = (FindEndpoint)getEndpoint();
    return endpoint.insertCharacter(character);
  }

  public InsertCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
