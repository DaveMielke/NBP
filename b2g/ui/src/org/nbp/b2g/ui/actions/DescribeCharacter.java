package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.UnicodeUtilities;

public class DescribeCharacter extends CursorKeyAction {
  private static String normalizeName (String name) {
    return name.replace('_', ' ').toLowerCase();
  }

  private static void startLine (StringBuilder sb, int label) {
    sb.append('\n');
    sb.append(getString(label));
    sb.append(": ");
  }

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    Character character = endpoint.getLineText().charAt(offset);
    StringBuilder sb = new StringBuilder();

    {
      String name = Character.getName(character);

      if (name != null) {
        name = name.toLowerCase();
      } else {
        name = getString(R.string.message_no_character_name);
      }

      sb.append(name);
    }

    startLine(sb, R.string.DescribeCharacter_label_codepoint);
    sb.append(Characters.toUnicodeString(character));

    if (ApplicationSettings.EXTRA_INDICATORS) {
      String decomposition = UnicodeUtilities.decompose(character);

      if (!decomposition.equals(Character.toString(character))) {
        sb.append(" ->");
        int count = decomposition.length();

        for (int index=0; index<count; index+=1) {
          sb.append(' ');
          sb.append(Characters.toUnicodeString(decomposition.charAt(index)));
        }
      }
    }

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

    return Endpoints.setPopupEndpoint(sb.toString());
  }

  public DescribeCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
