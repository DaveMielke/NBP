package org.nbp.b2g.ui;

import org.liblouis.TranslatorIdentifier;

public enum PhoneticAlphabet {
  // order is mportant
  OFF(null),
  BASIC(TranslatorIdentifier.IPA_BASE),
  EXTENDED(TranslatorIdentifier.IPA_EXTEND),
  ; // end of enumeration

  private final TranslatorIdentifier translatorIdentifier;

  PhoneticAlphabet (TranslatorIdentifier identifier) {
    translatorIdentifier = identifier;
  }

  public final TranslatorIdentifier getTranslatorIdentifier () {
    return translatorIdentifier;
  }
}
