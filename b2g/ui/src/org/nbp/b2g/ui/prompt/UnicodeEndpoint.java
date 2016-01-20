package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class UnicodeEndpoint extends PromptEndpoint {
  private final static String LOG_TAG = UnicodeEndpoint.class.getName();

  private final static int RADIX = 16;

  private final Character toCharacter (String digits) {
    if (digits.isEmpty()) return null;
    return (char)Integer.parseInt(digits, RADIX);
  }

  @Override
  protected String getTrailer () {
    StringBuilder sb = new StringBuilder();
    Character character = toCharacter(getResponse());

    if (character != null) {
      String name = Character.getName(character).toLowerCase();

      sb.append(' ');
      sb.append(character);

      if ((name != null) && !name.isEmpty()) {
        sb.append('\n');
        sb.append(name);
      }
    }

    return sb.toString();
  }

  @Override
  protected boolean canInsertText (CharSequence text) {
    if (text.length() == 0) return true;

    final int length = text.length();
    if (length != 1) return false;

    char character = text.charAt(0);
    if (Character.digit(character, RADIX) < 0) return false;

    return (getResponseLength() + length) <= 4;
  }

  @Override
  protected final boolean handleResponse (String response) {
    Character character = toCharacter(response);
    if (character == null) return false;
    return Endpoints.host.get().insertText(character);
  }

  public UnicodeEndpoint () {
    super(R.string.prompt_unicode, Characters.UNICODE_PREFIX);
  }
}
