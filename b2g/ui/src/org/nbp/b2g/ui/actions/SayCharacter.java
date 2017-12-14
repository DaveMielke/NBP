package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayCharacter extends Action {
  protected String toString (char character) {
    return CharacterPhrase.get(character);
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();
    String[] characters;

  HAVE_CHARACTERS:
    synchronized (endpoint) {
      if (!endpoint.isInputArea()) return false;
      CharSequence text = endpoint.getText();

      int start = endpoint.getSelectionStart();
      if (!endpoint.isSelected(start)) return false;

      int end = endpoint.getSelectionEnd();
      if (!endpoint.isSelected(end)) return false;

      if (end == start) {
        if (end == text.length()) {
          characters = new String[] {
            getString(R.string.character_end)
          };

          break HAVE_CHARACTERS;
        }

        end += 1;
      }

      int count = end - start;
      characters = new String[count];

      for (int index=0; index<count; index+=1) {
        characters[index] = toString(text.charAt(start + index));
      }
    }

    return ApplicationUtilities.say(characters);
  }

  public SayCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
