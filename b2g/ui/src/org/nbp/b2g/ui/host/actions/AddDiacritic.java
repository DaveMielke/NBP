package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import org.nbp.common.UnicodeUtilities;

public class AddDiacritic extends DiacriticAction {
  protected final int getChooseMessage () {
    return R.string.diacritic_choose_add;
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
    StringBuilder decomposition = new StringBuilder();
    decomposition.append(base);
    decomposition.append(' ');

    for (Character diacritic : DiacriticUtilities.getSupportedDiacritics()) {
      decomposition.setCharAt(1, diacritic);
      mapDiacritic(map, diacritic, decomposition.toString());
    }
  }

  public AddDiacritic (Endpoint endpoint) {
    super(endpoint);
  }
}
