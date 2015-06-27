package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DescribeCharacter extends SpeechAction {
  @Override
  public boolean performAction (int cursorKey) {
    String name = "";

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        String line = endpoint.getLineText();
        int offset = endpoint.getLineIndent() + cursorKey;

        if (offset >= line.length()) return false;
        char character = line.charAt(offset);

        name = Character.getName(character).toLowerCase();
      }
    }

    if (name == null) name = ApplicationContext.getString(R.string.message_no_character_name);
    Endpoints.setPopupEndpoint(name);
    return true;
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
