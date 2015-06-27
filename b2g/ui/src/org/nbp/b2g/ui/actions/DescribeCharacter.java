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
    {
      String name = Character.getName(character).toLowerCase();
      if (name == null) name = ApplicationContext.getString(R.string.message_no_character_name);
      sb.append(name);
    }

    sb.append(String.format("\nValue: U+%04X", (int)character));

    {
      Character.UnicodeBlock block = Character.UnicodeBlock.of(character);

      if (block != null) {
        sb.append("\nBlock: ");
        sb.append(block.toString().replace('_', ' ').toLowerCase());
      }
    }

    {
      int value = Character.getType(character);

      if (value != Character.UNASSIGNED) {
        sb.append("\nCategory: ");
        String name = UnicodeUtilities.getCategoryName(value);

        if (name != null) {
          sb.append(name);
        } else {
          sb.append(value);
        }
      }
    }

    {
      int value = Character.getDirectionality(character);

      if (value != Character.DIRECTIONALITY_UNDEFINED) {
        sb.append("\nDirectionality: ");
        String name = UnicodeUtilities.getDirectionalityName(value);

        if (name != null) {
          sb.append(name);
        } else {
          sb.append(value);
        }
      }
    }

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
