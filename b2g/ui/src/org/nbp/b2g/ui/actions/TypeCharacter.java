package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.text.SpannableStringBuilder;

public class TypeCharacter extends InputAction {
  private final static String LOG_TAG = TypeCharacter.class.getName();

  private boolean typeCharacter (Endpoint endpoint, char character, boolean literaryBraille) {
    {
      ModifierAction control = (ModifierAction)getAction(TypeControl.class);

      if (control != null) {
        if (control.getState()) {
          if ((character >= 0X40) && (character <= 0X7E)) {
            character &= 0X1F;
          } else if (character == 0X3F) {
            character |= 0X40;
          } else {
            Characters.logProblem(LOG_TAG, character, "no control mapping for character");
            return false;
          }
        }
      }
    }

    if (!literaryBraille) {
      Characters.logAction(LOG_TAG, character, "typing text");
      return endpoint.insertText(character);
    }

    if (endpoint.isInputArea()) {
      int start = endpoint.getSelectionStart();
      int end = endpoint.getSelectionEnd();

      if (Endpoint.isSelected(start) && endpoint.isSelected(end)) {
        boolean isCursor = start == end;
        boolean atStartOfText = start == 0;
        boolean atEndOfLine = end == endpoint.getLineEnd();

        {
          int offset = endpoint.getLineStart();
          start -= offset;
          end -= offset;
        }

        end = endpoint.findEndBrailleOffset(isCursor? start: end);
        start = endpoint.findFirstBrailleOffset(start);

        if (isCursor && atEndOfLine) {
          int length = endpoint.getBrailleLength();

          if (end < length) {
            start = end = length;
            atStartOfText = false;
          }
        }

        if (ApplicationSettings.LOG_ACTIONS) {
          Log.v(LOG_TAG, String.format(
            "inserting braille: %c @ [%d:%d]", character, start, end
          ));
        }

        if (isCursor && atStartOfText && (endpoint.getBrailleLength() > 0)) {
          TranslationUtilities.cacheBraille(character);
        }

        CharSequence braille = endpoint.getBrailleCharacters();
        SpannableStringBuilder sb = new SpannableStringBuilder(braille);
        sb.replace(start, end, Character.toString(character));
        return endpoint.setBrailleCharacters(sb.subSequence(0, sb.length()));
      }
    }

    {
      CharSequence text = TranslationUtilities.newTextTranslation(character).getTextAsString();

      if (ApplicationSettings.LOG_ACTIONS) {
        Log.v(LOG_TAG, String.format(
          "typing braille: %c -> %s", character, text
        ));
      }

      return endpoint.insertText(text);
    }
  }

  @Override
  protected final boolean performInputAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      TypingMode typingMode = ApplicationSettings.TYPING_MODE;
      boolean literaryBraille = ApplicationSettings.LITERARY_BRAILLE && (typingMode == TypingMode.TEXT);

      if (endpoint.isPasswordField()) {
        typingMode = TypingMode.TEXT;
        literaryBraille = false;
      } else if (literaryBraille) {
        typingMode = TypingMode.BRAILLE;
      }

      int keyMask = getNavigationKeys();
      Character character;

      switch (typingMode) {
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
          Log.w(LOG_TAG, ("unsupported typing mode: " + typingMode.name()));
          return false;
      }

      return typeCharacter(endpoint, character, literaryBraille);
    }
  }

  @Override
  protected final boolean performNonInputAction () {
    int keyMask = getNavigationKeys();
    if (!KeyMask.isDots(keyMask)) return false;

    keyMask &= ~KeyMask.SPACE;
    if (keyMask == 0) return false;

    return getEndpoint().handleDotKeys(keyMask);
  }

  public TypeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
