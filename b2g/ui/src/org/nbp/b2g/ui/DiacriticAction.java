package org.nbp.b2g.ui;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.nbp.common.UnicodeUtilities;

public abstract class DiacriticAction extends InputAction {
  protected static class DiacriticMap extends HashMap<Character, Character> {
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
  public final boolean performInputAction (final Endpoint endpoint) {
    if (endpoint.hasCursor()) {
      final int end = endpoint.getSelectionEnd();

      if (end > 0) {
        final int start = end - 1;

        final DiacriticMap diacriticMap = getDiacriticMap(endpoint.getText().charAt(start));
        if (diacriticMap == null) return false;

        Set<Character> diacriticSet = diacriticMap.keySet();
        diacriticSet.retainAll(DiacriticUtilities.getSupportedDiacritics());
        if (diacriticSet.isEmpty()) return false;

        final Character[] diacriticArray = diacriticSet.toArray(new Character[diacriticSet.size()]);
        DiacriticUtilities.sortDiacriticsByName(diacriticArray);

        StringBuilder message = new StringBuilder();
        message.append(getString(getChooseMessage()));

        for (Character diacritic : diacriticArray) {
          message.append('\n');
          message.append(DiacriticUtilities.getName(diacritic));
        }

        Endpoints.setPopupEndpoint(message.toString(), 1,
          new PopupClickHandler() {
            @Override
            public boolean handleClick (int index) {
              char character = diacriticMap.get(diacriticArray[index]);
              return endpoint.replaceText(start, end, Character.toString(character));
            }
          }
        );

        return true;
      }
    }

    return false;
  }

  protected final boolean mapDiacritic (DiacriticMap map, char diacritic, String decomposition) {
    String composition = UnicodeUtilities.compose(decomposition);
    if (composition == null) return false;
    if (composition.length() != 1) return false;

    map.put(diacritic, composition.charAt(0));
    return true;
  }

  protected DiacriticAction (Endpoint endpoint) {
    super(endpoint);
  }
}
