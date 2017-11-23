package org.nbp.b2g.ui;

public enum CharacterCode {
  LOCALE(null),
  EN("en"),
  EN_CA("en_CA"),
  EN_US("en_US"),
  PINYIN("pinyin"),
  ZH_CN("zh_CN"),
  ; // end of enumeration

  private final String codeName;
  private Characters characters = null;

  CharacterCode (String name) {
    codeName = name;
  }

  public final String getName () {
    return codeName;
  }

  public final Characters getCharacters () {
    synchronized (this) {
      if (characters == null) {
        characters = (codeName != null)?
                     new Characters(codeName):
                     new Characters();
      }

      return characters;
    }
  }

  public final void reloadCharacters () {
    synchronized (this) {
      characters = null;
    }
  }
}
