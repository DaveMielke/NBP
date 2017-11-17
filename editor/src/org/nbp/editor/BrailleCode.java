package org.nbp.editor;

import org.liblouis.Translator;
import org.liblouis.TranslatorEnumeration;

public enum BrailleCode {
  NONE(null),
  UEB(TranslatorEnumeration.EN_UEB_G2),
  EBAE(TranslatorEnumeration.EN_US_G2),
  ; // end of enumeration

  private final TranslatorEnumeration translatorEnumeration;

  BrailleCode (TranslatorEnumeration translator) {
    translatorEnumeration = translator;
  }

  public final TranslatorEnumeration getTranslatorEnumeration () {
    return translatorEnumeration;
  }

  public final Translator getTranslator () {
    if (translatorEnumeration == null) return null;
    return translatorEnumeration.getTranslator();
  }
}
