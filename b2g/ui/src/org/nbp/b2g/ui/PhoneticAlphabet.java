package org.nbp.b2g.ui;

import org.liblouis.TranslatorIdentifier;

public enum PhoneticAlphabet {
  OFF(null),
  SAFE(TranslatorIdentifier.IPA_SAFE),
  ALL(TranslatorIdentifier.IPA_ALL),
  ; // end of enumeration

  private final TranslatorIdentifier translatorIdentifier;

  PhoneticAlphabet (TranslatorIdentifier identifier) {
    translatorIdentifier = identifier;
  }

  public final TranslatorIdentifier getTranslatorIdentifier () {
    return translatorIdentifier;
  }
}
