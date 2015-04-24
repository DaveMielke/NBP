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
        name = Character.getName(line.charAt(offset)).toLowerCase();
      }
    }

    if (name == null) name = "no character name";
    ApplicationUtilities.message(name);
    return true;
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
