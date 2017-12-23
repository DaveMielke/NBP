package org.nbp.b2g.ui;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.nbp.common.CharacterUtilities;

public abstract class DiacriticUtilities {
  private final static Set<Character> supportedDiacritics;
  private final static Map<Character, String> diacriticNames;
  private final static Map<String, Character> diacriticCharacters;

  private final static void defineDiacritic (Character character, int resource) {
    String name = ApplicationContext.getString(resource);
    diacriticNames.put(character, name);
    diacriticCharacters.put(name, character);
  }

  static {
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
      CharacterUtilities.COMB_DASIA,
      R.string.character_dasia
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
      CharacterUtilities.COMB_PERISPOMENI,
      R.string.character_perispomeni
    );

    defineDiacritic(
      CharacterUtilities.COMB_PSILI,
      R.string.character_psili
    );

    defineDiacritic(
      CharacterUtilities.COMB_RING,
      R.string.character_ring
    );

    defineDiacritic(
      CharacterUtilities.COMB_TILDE,
      R.string.character_tilde
    );

    defineDiacritic(
      CharacterUtilities.COMB_YPOGEGRAMMENI,
      R.string.character_ypogegrammeni
    );

    supportedDiacritics = diacriticNames.keySet();
  }

  public final static Set<Character> getSupportedDiacritics () {
    return supportedDiacritics;
  }

  public final static String getDiacriticName (char diacritic) {
    return diacriticNames.get(diacritic);
  }

  private DiacriticUtilities () {
  }
}
