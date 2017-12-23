package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.nbp.common.CharacterUtilities;

public abstract class DiacriticAction extends Action {
  private final static Object DIACRITIC_LOCK = new Object();
  private static Map<Character, String> diacriticNames = null;
  private static Map<String, Character> diacriticCharacters = null;

  private final static void defineDiacritic (Character character, int resource) {
    String name = getString(resource);
    diacriticNames.put(character, name);
    diacriticCharacters.put(name, character);
  }

  private final static void defineDiacritics () {
    synchronized (DIACRITIC_LOCK) {
      if (diacriticNames == null) {
        diacriticNames = new HashMap<Character, String>();
        diacriticCharacters = new HashMap<String, Character>();

        defineDiacritic(
          CharacterUtilities.COMB_ACUTE,
          R.string.character_acute
        );

        defineDiacritic(
          CharacterUtilities.COMB_BREVE,
          R.string.character_breve
        );

        defineDiacritic(
          CharacterUtilities.COMB_CARON,
          R.string.character_caron
        );

        defineDiacritic(
          CharacterUtilities.COMB_CEDILLA,
          R.string.character_cedilla
        );

        defineDiacritic(
          CharacterUtilities.COMB_CIRCUMFLEX,
          R.string.character_circumflex
        );

        defineDiacritic(
          CharacterUtilities.COMB_DIAERESIS,
          R.string.character_diaeresis
        );

        defineDiacritic(
          CharacterUtilities.COMB_GRAVE,
          R.string.character_grave
        );

        defineDiacritic(
          CharacterUtilities.COMB_MACRON,
          R.string.character_macron
        );

        defineDiacritic(
          CharacterUtilities.COMB_RING,
          R.string.character_ring
        );

        defineDiacritic(
          CharacterUtilities.COMB_TILDE,
          R.string.character_tilde
        );
      }
    }
  }

  protected final Set<Character> getDiacritics () {
    return diacriticNames.keySet();
  }

  protected static class DiacriticMap extends LinkedHashMap<Character, Character> {
    public DiacriticMap () {
      super();
    }
  }

  protected static class CharacterMap extends HashMap<Character, DiacriticMap> {
    public CharacterMap () {
      super();
    }
  }

  protected abstract int getChooseMessage ();
  protected abstract CharacterMap getCharacterMap ();
  protected abstract void makeDiacriticMap (DiacriticMap map, Character base);

  private final DiacriticMap getDiacriticMap (Character character) {
    CharacterMap characterMap = getCharacterMap();

    synchronized (characterMap) {
      DiacriticMap diacriticMap = characterMap.get(character);
      if (diacriticMap != null) return diacriticMap;
      if (characterMap.containsKey(character)) return null;

      diacriticMap = new DiacriticMap();
      makeDiacriticMap(diacriticMap, character);
      if (diacriticMap.isEmpty()) diacriticMap = null;
      characterMap.put(character, diacriticMap);
      return diacriticMap;
    }
  }

  @Override
  public final boolean performAction () {
    final Endpoint endpoint = Endpoints.host.get();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        if (endpoint.hasCursor()) {
          final int end = endpoint.getSelectionEnd();

          if (end > 0) {
            final int start = end - 1;

            final DiacriticMap diacriticMap = getDiacriticMap(endpoint.getText().charAt(start));
            if (diacriticMap == null) return false;

            Set<Character> diacriticSet = diacriticMap.keySet();
            if (diacriticSet.isEmpty()) return false;
            final Character[] diacriticArray = diacriticSet.toArray(new Character[diacriticSet.size()]);

            StringBuilder message = new StringBuilder();
            message.append(getString(getChooseMessage()));

            for (Character diacritic : diacriticArray) {
              message.append('\n');
              message.append(diacriticNames.get(diacritic));
            }

            Endpoints.setPopupEndpoint(message.toString(),
              new PopupClickHandler() {
                @Override
                public boolean handleClick (int index) {
                  if (index == 0) return false;
                  char character = diacriticMap.get(diacriticArray[--index]);
                  Endpoints.setHostEndpoint();
                  return endpoint.replaceText(start, end, Character.toString(character));
                }
              }
            );

            return true;
          }
        }
      }
    }

    return false;
  }

  protected DiacriticAction (Endpoint endpoint) {
    super(endpoint, false);
    defineDiacritics();
  }
}
