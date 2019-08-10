package org.nbp.editor;

import org.liblouis.Translator;
import org.liblouis.TranslatorIdentifier;

public enum BrailleCode {
  UEB(TranslatorIdentifier.EN_UEB_G2),
  EBAE(TranslatorIdentifier.EN_US_G2),
  EL(TranslatorIdentifier.EL),
  GRC_INTL_EN(TranslatorIdentifier.GRC_INTL_EN),
  ; // end of enumeration

  private final TranslatorIdentifier translatorIdentifier;

  BrailleCode (TranslatorIdentifier identifier) {
    translatorIdentifier = identifier;
  }

  public final TranslatorIdentifier getTranslatorIdentifier () {
    return translatorIdentifier;
  }

  public final Translator getTranslator () {
    if (translatorIdentifier == null) return null;
    return translatorIdentifier.getTranslator();
  }

  public final String getDescription () {
    return getTranslatorIdentifier().getDescription();
  }
}
