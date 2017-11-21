package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayCharacter extends Action {
  protected String toString (char character) {
    return ApplicationUtilities.toString(character);
  }

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();
    String[] characters;

  HAVE_CHARACTERS:
    synchronized (endpoint) {
      CharSequence text = endpoint.getText();
      int start = endpoint.getSelectionStart();
      int end = endpoint.getSelectionEnd();

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

    ApplicationUtilities.say(characters);
    return true;
  }

  public SayCharacter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
