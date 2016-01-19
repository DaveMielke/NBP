package org.nbp.b2g.ui;

import org.liblouis.TranslationTable;

public enum BrailleCode {
  EN_UEB_G1(
    TranslationTable.EN_UEB_G1
  ),

  EN_UEB_G2(
    TranslationTable.EN_UEB_G2
  ),

  EN_US_G1(
    TranslationTable.EN_US_G1
  ),

  EN_US_G2(
    TranslationTable.EN_US_G2
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
