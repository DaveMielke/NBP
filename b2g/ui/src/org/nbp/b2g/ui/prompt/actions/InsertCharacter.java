package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

public class InsertCharacter extends InsertCharacterAction {
  @Override
  public boolean insertCharacter (char character) {
    PromptEndpoint endpoint = (PromptEndpoint)getEndpoint();
    return endpoint.insertCharacter(character);
  }

  public InsertCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
