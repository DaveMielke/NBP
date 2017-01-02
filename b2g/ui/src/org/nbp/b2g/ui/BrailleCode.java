package org.nbp.b2g.ui;

import org.liblouis.TranslationTable;

public enum BrailleCode {
  EN_UEB_G1(
    TranslationTable.EN_UEB_G1
  ),

  EN_UEB_G2(
    TranslationTable.EN_UEB_G2
  ),

  EN_EBAE_G1(
    TranslationTable.EN_US_G1
  ),

  EN_EBAE_G2(
    TranslationTable.EN_US_G2
  ),

  FR_BFU_G2(
    TranslationTable.FR_BFU_G2
  ),

  DE_G1(
    TranslationTable.DE_DE_G1
  ),

  DE_G2(
    TranslationTable.DE_DE_G2
  ),

  GR(
    TranslationTable.GR
  ),

  HE(
    TranslationTable.HE
  ),

  ES_G1(
    TranslationTable.ES_G1
  ),

  ; // endof enumeration

  private final TranslationTable translationTable;

  public final TranslationTable getTranslationTable () {
    return translationTable;
  }

  BrailleCode (TranslationTable table) {
    translationTable = table;
  }
}
