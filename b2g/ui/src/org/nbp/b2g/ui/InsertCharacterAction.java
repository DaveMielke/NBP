package org.nbp.b2g.ui;

import android.util.Log;

public class InsertCharacterAction extends ScreenAction {
  private final static String LOG_TAG = InsertCharacterAction.class.getName();

  private Characters getCharacters () {
    return Context.getCurrentContext().getCharacters();
  }

  @Override
  public boolean parseOperand (int keyMask, String operand) {
    return getCharacters().setCharacter(keyMask, operand);
  }

  protected boolean insertCharacter (char character) {
    return false;
  }

  @Override
  public boolean performAction () {
    int keyMask = getNavigationKeys();
    Character character = getCharacters().getCharacter(keyMask);

    if (character == null) {
      Log.w(LOG_TAG, String.format("not mapped to a character: 0X%02X", keyMask));
    } else if (insertCharacter(character)) {
      return true;
    }

    return false;
  }

  public InsertCharacterAction () {
    super(false);
  }
}
