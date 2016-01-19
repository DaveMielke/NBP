package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.text.SpannableStringBuilder;

import org.liblouis.BrailleTranslation;

public class TypeCharacter extends Action {
  private final static String LOG_TAG = TypeCharacter.class.getName();

  private boolean typeCharacter (char character) {
    Endpoint endpoint = getEndpoint();

    {
      ModifierAction control = (ModifierAction)getAction(TypeControl.class);

      if (control != null) {
        if (control.getState()) {
          if ((character >= 0X40) && (character <= 0X7E)) {
            character &= 0X1F;
          } else if (character == 0X3F) {
            character |= 0X40;
          } else {
            Log.w(LOG_TAG, String.format(
              "no control mapping for character: %s%04X",
              Characters.UNICODE_PREFIX, (int)character
            ));

            return false;
          }
        }
      }
    }

    synchronized (endpoint) {
      BrailleTranslation brl = endpoint.getBrailleTranslation();

      if (brl == null) {
        return endpoint.insertText(character);
      }

      if (endpoint.isInputArea()) {
        int start = endpoint.getSelectionStart();
        int end = endpoint.getSelectionEnd();

        if (endpoint.isSelected(start) && endpoint.isSelected(end)) {
          SpannableStringBuilder sb = new SpannableStringBuilder(brl.getBrailleWithSpans());

          sb.replace(
            Braille.findFirstOffset(brl, start),
            Braille.findEndOffset(brl, end),
            Character.toString(character)
          );

          return endpoint.replaceLine(TranslationUtilities.newTextTranslation(sb.subSequence(0, sb.length())).getTextWithSpans());
        }
      }

      return endpoint.insertText(TranslationUtilities.newTextTranslation(character).getTextWithSpans());
    }
  }

  @Override
  public boolean performAction () {
    InputMode inputMode = ApplicationSettings.INPUT_MODE;
    int keyMask = getNavigationKeys();
    Character character;

    if (ApplicationSettings.LITERARY_BRAILLE) {
      inputMode = InputMode.BRAILLE;
    }

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

    return typeCharacter(character);
  }

  public TypeCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
