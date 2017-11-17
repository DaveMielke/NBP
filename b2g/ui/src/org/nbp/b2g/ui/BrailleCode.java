package org.nbp.b2g.ui;

import org.liblouis.Translator;
import org.liblouis.TranslatorEnumeration;

public enum BrailleCode {
  EN_UEB_G1(
    TranslatorEnumeration.EN_UEB_G1
  ),

  EN_UEB_G2(
    TranslatorEnumeration.EN_UEB_G2
  ),

  EN_EBAE_G1(
    TranslatorEnumeration.EN_US_G1
  ),

  EN_EBAE_G2(
    TranslatorEnumeration.EN_US_G2
  ),

  FR_BFU_G2(
    TranslatorEnumeration.FR_BFU_G2
  ),

  DE_G1(
    TranslatorEnumeration.DE_DE_G1
  ),

  DE_G2(
    TranslatorEnumeration.DE_DE_G2
  ),

  EL(
    TranslatorEnumeration.EL
  ),

  HE(
    TranslatorEnumeration.HE
  ),

  ES_G1(
    TranslatorEnumeration.ES_G1
  ),

  ; // end of enumeration

  private final TranslatorEnumeration translatorEnumeration;

  BrailleCode (TranslatorEnumeration translator) {
    translatorEnumeration = translator;
  }

  public final TranslatorEnumeration getTranslatorEnumeration () {
    return translatorEnumeration;
  }

  public final Translator getTranslator () {
    return translatorEnumeration.getTranslator();
  }
}
