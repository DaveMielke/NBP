package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.Braille;

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
        endpoint.adjustScroll(start);

        {
          int lineLength = endpoint.getLineLength();
          int lineStart = endpoint.getLineStart();

          {
            int from = lineStart + lineLength;

            if (from < end) {
              if (!endpoint.deleteText(from, end)) return false;
              endpoint.adjustScroll(start);
            }
          }

          start -= lineStart;
          end = Math.min((end - lineStart), lineLength);
        }

        CharSequence braille = endpoint.isHintText()? "": endpoint.getBrailleCharacters();
        start = endpoint.findFirstBrailleOffset(start);

        if (isCursor) {
          if (character != Braille.UNICODE_ROW) {
            int brailleLength = braille.length();
            int brailleOffset = start;
            int lineOffset = endpoint.getLineOffset(brailleOffset);
            CharSequence text = endpoint.getLineText();

            if ((lineOffset == text.length()) || Endpoint.isWordBreak(text.charAt(lineOffset))) {
              while (brailleOffset < brailleLength) {
                if (endpoint.getLineOffset(++brailleOffset) != lineOffset) break;
                start = brailleOffset;
              }
            }
          }

          end = start;
        } else {
          end = endpoint.findFirstBrailleOffset(end);
        }

        if (ApplicationSettings.LOG_ACTIONS) {
          Log.v(LOG_TAG, String.format(
            "inserting braille: %c @ [%d:%d]", character, start, end
          ));
        }

        SpannableStringBuilder sb = new SpannableStringBuilder(braille);
        sb.replace(start, end, Character.toString(character));
        return endpoint.setBrailleCharacters(sb, (start + 1));
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
  protected final boolean performInputAction (Endpoint endpoint) {
    TypingMode typingMode = ApplicationSettings.TYPING_MODE;
    boolean literaryBraille = ApplicationSettings.LITERARY_BRAILLE && (typingMode == TypingMode.TEXT);

    if (endpoint.isPasswordField()) {
      typingMode = TypingMode.TEXT;
      literaryBraille = false;
    } else if (literaryBraille) {
      typingMode = TypingMode.BRAILLE;
    }

    KeySet keys = getNavigationKeys();
    Character character;

    switch (typingMode) {
      case TEXT: {
        character = Characters.getCharacters().getCharacter(keys);

        if (character == null) {
          Log.w(LOG_TAG, String.format(
            "not mapped to a character: %s",
            keys.toString()
          ));

          return false;
        }

        break;
      }

      case BRAILLE: {
        Byte dots = keys.toDots();

        if (dots == null) {
          Log.w(LOG_TAG, String.format(
            "not a braille character: %s",
            keys.toString()
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

  @Override
  protected final boolean performNonInputAction (Endpoint endpoint) {
    Byte dots = getNavigationKeys().toDots();
    if (dots == null) return false;
    if (dots == 0) return false;
    return endpoint.handleDotKeys(dots);
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeCharacter (Endpoint endpoint) {
    super(endpoint);
  }
}
