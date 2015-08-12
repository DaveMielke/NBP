package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DescribeCharacter extends Action {
  private static String normalizeName (String name) {
    return name.replace('_', ' ').toLowerCase();
  }

  private static void startLine (StringBuilder sb, int label) {
    sb.append('\n');
    sb.append(ApplicationContext.getString(label));
    sb.append(": ");
  }

  @Override
  public boolean performAction (int cursorKey) {
    Character character;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        CharSequence line = endpoint.getLineText();
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

    startLine(sb, R.string.DescribeCharacter_label_codepoint);
    sb.append(Characters.UNICODE_PREFIX);
    sb.append(String.format("%04X", (int)character));

    {
      Character.UnicodeBlock block = Character.UnicodeBlock.of(character);

      if (block != null) {
        startLine(sb, R.string.DescribeCharacter_label_block);
        sb.append(normalizeName(block.toString()));
      }
    }

    {
      int value = Character.getType(character);

      if (value != Character.UNASSIGNED) {
        startLine(sb, R.string.DescribeCharacter_label_category);
        String name = UnicodeUtilities.getCategoryName(value);

        if (name != null) {
          sb.append(normalizeName(name));
        } else {
          sb.append(value);
        }
      }
    }

    {
      int value = Character.getDirectionality(character);

      if (value != Character.DIRECTIONALITY_UNDEFINED) {
        startLine(sb, R.string.DescribeCharacter_label_directionality);
        String name = UnicodeUtilities.getDirectionalityName(value);

        if (name != null) {
          sb.append(normalizeName(name));
        } else {
          sb.append(value);
        }
      }
    }

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
