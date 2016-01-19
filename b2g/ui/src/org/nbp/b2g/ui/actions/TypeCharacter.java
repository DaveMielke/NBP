package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.text.SpannableStringBuilder;

import org.liblouis.BrailleTranslation;

public class TypeCharacter extends Action {
  private final static String LOG_TAG = TypeCharacter.class.getName();

  private final boolean replaceLine (Endpoint endpoint, CharSequence newText) {
    CharSequence oldText = endpoint.getLineText();
    int oldTo = oldText.length();
    int newTo = newText.length();
    int from = 0;

    while ((from < oldTo) && (from < newTo)) {
      if (oldText.charAt(from) != newText.charAt(from)) break;
      from += 1;
    }

    while ((from < oldTo) && (from < newTo)) {
      int oldLast = oldTo - 1;
      int newLast = newTo - 1;
      if (oldText.charAt(oldLast) != newText.charAt(newLast)) break;
      oldTo = oldLast;
      newTo = newLast;
    }

    boolean removing = oldTo > from;
    boolean inserting = newTo > from;
    if (!(inserting || removing)) return true;
    CharSequence newSegment = newText.subSequence(from, newTo);

    if (ApplicationSettings.LOG_ACTIONS) {
      CharSequence oldSegment = oldText.subSequence(from, oldTo);

      if (removing) {
        if (inserting) {
          Log.v(LOG_TAG, String.format(
            "replacing text: %s -> %s", oldSegment, newSegment
          ));
        } else {
          Log.v(LOG_TAG, String.format(
            "removing text: %s", oldSegment
          ));
        }
      } else {
        Log.v(LOG_TAG, String.format(
          "inserting text: %s", newSegment
        ));
      }
    }

    int start = endpoint.getLineStart();
    return endpoint.replaceText((start + from), (start + oldTo), newSegment);
  }

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
            Characters.logProblem(LOG_TAG, character, "no control mapping for character");
            return false;
          }
        }
      }
    }

    synchronized (endpoint) {
      BrailleTranslation brl = endpoint.getBrailleTranslation();

      if (brl == null) {
        Characters.logAction(LOG_TAG, character, "typing text");
        return endpoint.insertText(character);
      }

      if (endpoint.isInputArea()) {
        int start = endpoint.getSelectionStart();
        int end = endpoint.getSelectionEnd();

        if (Endpoint.isSelected(start) && endpoint.isSelected(end)) {
          boolean isCursor = start == end;
          start = Braille.findFirstOffset(brl, start);
          end = isCursor? start: Braille.findEndOffset(brl, end);
          CharSequence oldBraille = brl.getBrailleWithSpans();

          if (ApplicationSettings.LOG_ACTIONS) {
            if (isCursor) {
              Log.v(LOG_TAG, String.format(
                "inserting braille: %c", character
              ));
            } else {
              CharSequence oldSegment = oldBraille.subSequence(start, end);

              if (oldSegment.length() == 0) {
                Log.v(LOG_TAG, String.format(
                  "removing braille: %s", oldSegment
                ));
              } else {
                Log.v(LOG_TAG, String.format(
                  "replacing braille: %s -> %c", oldSegment, character
                ));
              }
            }
          }

          SpannableStringBuilder sb = new SpannableStringBuilder(oldBraille);
          sb.replace(start, end, Character.toString(character));

          return replaceLine(
            endpoint,
            TranslationUtilities.newTextTranslation(sb.subSequence(0, sb.length()))
                                .getTextWithSpans()
          );
        }
      }

      {
        CharSequence text = TranslationUtilities.newTextTranslation(character).getTextWithSpans();

        if (ApplicationSettings.LOG_ACTIONS) {
          Log.v(LOG_TAG, String.format(
            "typing braille: %c -> %s", character, text
          ));
        }

        return endpoint.insertText(text);
      }
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
