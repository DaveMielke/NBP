package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.CharacterUtilities;
import org.nbp.common.UnicodeUtilities;

public abstract class CharacterPhrase {
  private CharacterPhrase () {
  }

  public static class Dictionary extends HashMap<Character, String> {
    public Dictionary () {
      super();
    }
  }

  public static class Cache extends HashMap<Character, String> {
    public Cache () {
      super();
    }
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
    }
  };

  public final static String get (char character, Dictionary... dictionaries) {
    StringBuilder phrase = new StringBuilder();
    String characters = UnicodeUtilities.decompose(character);
    int count = characters.length();

  CHARACTER_LOOP:
    for (int index=0; index<count; index+=1) {
      character = characters.charAt(index);

      if (phrase.length() > 0) phrase.append(' ');
      if (Character.isUpperCase(character)) phrase.append("cap ");

      if (dictionaries != null) {
        for (Dictionary dictionary : dictionaries) {
          String word = dictionary.get(character);

          if (word != null) {
            phrase.append(word);
            continue CHARACTER_LOOP;
          }
        }
      }

      {
        String name = DiacriticUtilities.getDiacriticName(character);

        if (name != null) {
          phrase.append(name);
          continue CHARACTER_LOOP;
        }
      }

      final Character resourceKey = character;
      Integer resource = resourceCache.get(resourceKey);

      if (resource == null) {
        if (Character.isWhitespace(character)) {
          resource = SPACE;
        } else {
          phrase.append(character);
          continue CHARACTER_LOOP;
        }

        resourceCache.put(resourceKey, resource);
      }

      phrase.append(ApplicationContext.getString(resource));
    }

    return phrase.toString();
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
