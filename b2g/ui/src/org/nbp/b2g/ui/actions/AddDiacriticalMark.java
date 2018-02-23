package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.UnicodeUtilities;

public class AddDiacriticalMark extends DiacriticAction {
  protected final int getChooseMessage () {
    return R.string.popup_select_diacritic_add;
  }

  private final static Object MAP_LOCK = new Object();
  private static CharacterMap characterMap = null;

  @Override
  protected final CharacterMap getCharacterMap () {
    synchronized (MAP_LOCK) {
      if (characterMap == null) characterMap = new CharacterMap();
    }

    return characterMap;
  }

  @Override
  protected final void makeDiacriticMap (DiacriticMap map, Character base) {
    StringBuilder decomposition = new StringBuilder(UnicodeUtilities.decompose(Character.toString(base)));
    int length = decomposition.length();

    for (Character diacritic : DiacriticUtilities.getSupportedDiacritics()) {
      for (int index=1; index<=length; index+=1) {
        decomposition.insert(index, diacritic);
        boolean mapped = mapDiacritic(map, diacritic, decomposition.toString());
        decomposition.deleteCharAt(index);
        if (mapped) break;
      }
    }
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public AddDiacriticalMark (Endpoint endpoint) {
    super(endpoint);
  }
}
