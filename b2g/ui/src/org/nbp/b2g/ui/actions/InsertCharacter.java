package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class InsertCharacter extends Action {
  private final static String LOG_TAG = InsertCharacter.class.getName();

  private boolean insertCharacter (char character) {
    ModifierAction control = (ControlModifier)getAction(ControlModifier.class);

    if (control != null) {
      if (control.getState()) {
        if ((character >= 0X40) && (character <= 0X7E)) {
          character &= 0X1F;
        } else if (character == 0X3F) {
          character |= 0X40;
        } else {
          return false;
        }
      }
    }

    return getEndpoint().insertText(character);
  }

  @Override
  public boolean performAction () {
    InputMode inputMode = ApplicationSettings.INPUT_MODE;
    int keyMask = getNavigationKeys();
    Character character;

    switch (inputMode) {
      case TEXT: {
        character = Characters.getCharacters().toCharacter(keyMask);

        if (character == null) {
          Log.w(LOG_TAG, String.format(
            "not mapped to a character: %s",
            KeyMask.toString(keyMask)
          ));

          return false;
        }

        break;
      }

      case BRAILLE: {
        Byte dots = KeyMask.toDots(keyMask);

        if (dots == null) {
          Log.w(LOG_TAG, String.format(
            "not a braille character: %s",
            KeyMask.toString(keyMask)
          ));

          return false;
        }

        character = Braille.toCharacter(dots);
        break;
      }

      default:
        Log.w(LOG_TAG, "unsupported input mode: " + inputMode.name());
        return false;
    }

    return insertCharacter(character);
  }

  public InsertCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
