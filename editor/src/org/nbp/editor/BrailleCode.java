package org.nbp.editor;

import org.liblouis.TranslationEnumeration;
import org.liblouis.TranslationTable;

public enum BrailleCode {
  NONE(null),
  UEB(TranslationEnumeration.EN_UEB_G2),
  EBAE(TranslationEnumeration.EN_US_G2),
  ; // end of enumeration

  private final TranslationEnumeration translationEnumeration;

  BrailleCode (TranslationEnumeration table) {
    translationEnumeration = table;
  }

  public final TranslationEnumeration getTranslationEnumeration () {
    return translationEnumeration;
  }

  public final TranslationTable getTranslationTable () {
    if (translationEnumeration == null) return null;
    return translationEnumeration.getTranslationTable();
  }
}
