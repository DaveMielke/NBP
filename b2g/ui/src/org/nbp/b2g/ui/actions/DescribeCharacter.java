package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DescribeCharacter extends SpeechAction {
  @Override
  public boolean performAction (int cursorKey) {
    Character character;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        String line = endpoint.getLineText();
        int offset = endpoint.getLineIndent() + cursorKey;

        if (offset >= line.length()) return false;
        character = line.charAt(offset);
      }
    }

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("U+%04X", (int)character));

    {
      Character.UnicodeBlock ucb = Character.UnicodeBlock.of(character);

      if (ucb != null) {
        sb.append(": ");
        sb.append(ucb.toString().replace('_', ' ').toLowerCase());
      }
    }

    {
      String name = Character.getName(character).toLowerCase();
      if (name == null) name = ApplicationContext.getString(R.string.message_no_character_name);

      sb.append('\n');
      sb.append(name);
    }

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
