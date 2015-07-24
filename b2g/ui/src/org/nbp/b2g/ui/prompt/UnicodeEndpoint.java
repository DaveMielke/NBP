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
    Character character = toCharacter(getResponse());
    if (character == null) return "";
    return ": " + Character.getName(character);
  }

  @Override
  protected boolean canInsertText (String string) {
    final int length = string.length();
    if (length != 1) return false;

    char character = string.charAt(0);
    if (Character.digit(character, RADIX) < 0) return false;

    return (getResponseLength() + length) <= 4;
  }

  @Override
  public final boolean handleResponse (String response) {
    return false;
  }

  public UnicodeEndpoint () {
    super("Unicode");
  }
}
