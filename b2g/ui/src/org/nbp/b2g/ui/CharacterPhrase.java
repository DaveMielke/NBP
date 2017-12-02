package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.UnicodeUtilities;

public abstract class CharacterPhrase {
  private CharacterPhrase () {
  }

  public static class Dictionary extends HashMap<Character, String> {
  }

  public static class Cache extends HashMap<Character, String> {
  }

  private final static Integer SPACE = R.string.character_space;

  private final static Map<Character, Integer> resourceCache =
               new HashMap<Character, Integer>()
  {
    {
      {
        Integer a = R.string.character_a;
        put('a', a);
        put('A', a);
      }

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

      put('\u0342', R.string.character_perispomeni);
      put('\u0343', R.string.character_koronis);
      put('\u0344', R.string.character_dialytika_tonos);
      put('\u0345', R.string.character_ypogegrammeni);
    }
  };

  public final static String get (char character, Dictionary... dictionaries) {
    StringBuilder sb = new StringBuilder();
    String characters = UnicodeUtilities.decompose(character);
    int count = characters.length();

  CHARACTER_LOOP:
    for (int index=0; index<count; index+=1) {
      character = characters.charAt(index);

      if (sb.length() > 0) sb.append(' ');
      if (Character.isUpperCase(character)) sb.append("cap ");

      if (dictionaries != null) {
        for (Dictionary dictionary : dictionaries) {
          String word = dictionary.get(character);

          if (word != null) {
            sb.append(word);
            continue CHARACTER_LOOP;
          }
        }
      }

      final Character resourceKey = character;
      Integer resource = resourceCache.get(resourceKey);

      if (resource == null) {
        if (Character.isWhitespace(character)) {
          resource = SPACE;
        } else {
          sb.append(character);
          continue CHARACTER_LOOP;
        }

        resourceCache.put(resourceKey, resource);
      }

      sb.append(ApplicationContext.getString(resource));
    }

    return sb.toString();
  }

  public final static String get (char character, Cache cache, Dictionary... dictionaries) {
    synchronized (cache) {
      final Character key = character;
      String phrase = cache.get(key);

      if (phrase == null) {
        phrase = get(character, dictionaries);
        cache.put(key, phrase);
      }

      return phrase;
    }
  }

  private final static Cache phraseCache = new Cache();

  public final static String get (char character) {
    return get(character, phraseCache, null);
  }
}
