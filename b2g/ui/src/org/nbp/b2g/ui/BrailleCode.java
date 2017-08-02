package org.nbp.b2g.ui;

import org.liblouis.TranslationEnumeration;
import org.liblouis.TranslationTable;

public enum BrailleCode {
  EN_UEB_G1(
    TranslationEnumeration.EN_UEB_G1
  ),

  EN_UEB_G2(
    TranslationEnumeration.EN_UEB_G2
  ),

  EN_EBAE_G1(
    TranslationEnumeration.EN_US_G1
  ),

  EN_EBAE_G2(
    TranslationEnumeration.EN_US_G2
  ),

  FR_BFU_G2(
    TranslationEnumeration.FR_BFU_G2
  ),

  DE_G1(
    TranslationEnumeration.DE_DE_G1
  ),

  DE_G2(
    TranslationEnumeration.DE_DE_G2
  ),

  EL(
    TranslationEnumeration.EL
  ),

  HE(
    TranslationEnumeration.HE
  ),

  ES_G1(
    TranslationEnumeration.ES_G1
  ),

  ; // end of enumeration

  private final TranslationEnumeration translationEnumeration;

  BrailleCode (TranslationEnumeration table) {
    translationEnumeration = table;
  }

  public final TranslationEnumeration getTranslationEnumeration () {
    return translationEnumeration;
  }

  public final TranslationTable getTranslationTable () {
    return translationEnumeration.getTranslationTable();
  }
}
