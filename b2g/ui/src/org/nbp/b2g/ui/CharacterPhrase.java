package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.UnicodeUtilities;

public abstract class CharacterPhrase {
  private CharacterPhrase () {
  }

  private final static Map<Character, String> phrases =
               new HashMap<Character, String>();

  private final static Map<Character, Integer> resources =
               new HashMap<Character, Integer>() {
    {
      put('A', R.string.character_a);
      put('a', R.string.character_a);

      put('\n', R.string.character_newline);

      put('\u0300', R.string.character_grave);
      put('\u0301', R.string.character_acute);
      put('\u0302', R.string.character_circumflex);
      put('\u0303', R.string.character_tilde);
      put('\u0304', R.string.character_macron);
      put('\u0306', R.string.character_breve);
      put('\u0308', R.string.character_diaeresis);
      put('\u030A', R.string.character_ring);
      put('\u030C', R.string.character_caron);
      put('\u0327', R.string.character_cedilla);
    }
  };

  public final static String get (char character) {
    synchronized (phrases) {
      Character phraseKey = character;

      {
        String phrase = phrases.get(phraseKey);
        if (phrase != null) return phrase;
      }

      StringBuilder sb = new StringBuilder();
      String characters = UnicodeUtilities.decompose(character);
      int count = characters.length();

      for (int index=0; index<count; index+=1) {
        character = characters.charAt(index);
        Character resourceKey = character;
        Integer resource = resources.get(resourceKey);

        if (sb.length() > 0) sb.append(' ');
        if (Character.isUpperCase(character)) sb.append("cap ");

        if (resource == null) {
          if (Character.isWhitespace(character)) {
            resource = R.string.character_space;
          } else {
            sb.append(character);
            continue;
          }

          resources.put(resourceKey, resource);
        }

        sb.append(ApplicationContext.getString(resource));
      }

      String phrase = sb.toString();
      phrases.put(phraseKey, phrase);
      return phrase;
    }
  }
}
