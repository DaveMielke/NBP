package org.liblouis;

import java.util.Set;
import java.util.LinkedHashSet;

public enum TranslatorIdentifier {
  BRAILLE_PATTERNS(
    "braille-patterns.cti", R.string.louis_ttd_BRAILLE_PATTERNS
  ),

  IPA_SAFE(
    "IPA-unicode-range.uti", R.string.louis_ttd_IPA_SAFE
  ),

  IPA_ALL(
    "IPA.utb", R.string.louis_ttd_IPA_ALL
  ),

  AFR_ZA_G1(
    "afr-za-g1.ctb", R.string.louis_ttd_AFR_ZA_G1
  ),

  BG(
    "bg.ctb", R.string.louis_ttd_BG
  ),

  BH(
    "bh.ctb", R.string.louis_ttd_BH
  ),

  BO(
    "bo.ctb", R.string.louis_ttd_BO
  ),

  BOXES(
    "boxes.ctb", R.string.louis_ttd_BOXES
  ),

  CA_G1(
    "ca-g1.ctb", R.string.louis_ttd_CA_G1
  ),

  CHR_US_G1(
    "chr-us-g1.ctb", R.string.louis_ttd_CHR_US_G1
  ),

  CKB_G1(
    "ckb-g1.ctb", R.string.louis_ttd_CKB_G1
  ),

  CS_G1(
    "cs-g1.ctb", R.string.louis_ttd_CS_G1
  ),

  CY_CY_G2(
    "cy-cy-g2.ctb", R.string.louis_ttd_CY_CY_G2
  ),

  DA_DK_G26(
    "da-dk-g26.ctb", R.string.louis_ttd_DA_DK_G26
  ),

  DA_DK_G28(
    "da-dk-g28.ctb", R.string.louis_ttd_DA_DK_G28
  ),

  DA_LT(
    "da-lt.ctb", R.string.louis_ttd_DA_LT
  ),

  DE_DE_COMP8(
    "de-de-comp8.ctb", R.string.louis_ttd_DE_DE_COMP8
  ),

  DE_G1(
    "de-g1.ctb", R.string.louis_ttd_DE_G1
  ),

  DE_G2(
    "de-g2.ctb", R.string.louis_ttd_DE_G2
  ),

  DRA(
    "dra.ctb", R.string.louis_ttd_DRA
  ),

  EL(
    "el.ctb", R.string.louis_ttd_EL
  ),

  EN_CA(
    "en_CA.ctb", R.string.louis_ttd_EN_CA
  ),

  EN_CHESS(
    "en-chess.ctb", R.string.louis_ttd_EN_CHESS
  ),

  EN_GB_COMP8(
    "en-gb-comp8.ctb", R.string.louis_ttd_EN_GB_COMP8
  ),

  EN_GB_G2(
    "en-GB-g2.ctb", R.string.louis_ttd_EN_GB_G2
  ),

  EN_IN_G1(
    "en-in-g1.ctb", R.string.louis_ttd_EN_IN_G1
  ),

  EN_UEB_G1(
    "en-ueb-g1.ctb", R.string.louis_ttd_EN_UEB_G1
  ),

  EN_UEB_G2(
    "en-ueb-g2.ctb", R.string.louis_ttd_EN_UEB_G2
  ),

  EN_US_COMP6(
    "en-us-comp6.ctb", R.string.louis_ttd_EN_US_COMP6
  ),

  EN_US_COMP8(
    "en-us-comp8.ctb", R.string.louis_ttd_EN_US_COMP8
  ),

  EN_US_G1(
    "en-us-g1.ctb", R.string.louis_ttd_EN_US_G1
  ),

  EN_US_G2(
    "en-us-g2.ctb", R.string.louis_ttd_EN_US_G2
  ),

  EN_US_INTERLINE(
    "en-us-interline.ctb", R.string.louis_ttd_EN_US_INTERLINE
  ),

  EN_US_MATHTEXT(
    "en-us-mathtext.ctb", R.string.louis_ttd_EN_US_MATHTEXT
  ),

  EO_G1(
    "eo-g1.ctb", R.string.louis_ttd_EO_G1
  ),

  EO_G1_X_SYSTEM(
    "eo-g1-x-system.ctb", R.string.louis_ttd_EO_G1_X_SYSTEM
  ),

  ES_G1(
    "es-g1.ctb", R.string.louis_ttd_ES_G1
  ),

  ET(
    "et.ctb", R.string.louis_ttd_ET
  ),

  ETHIO_G1(
    "ethio-g1.ctb", R.string.louis_ttd_ETHIO_G1
  ),

  FI1(
    "fi1.ctb", R.string.louis_ttd_FI1
  ),

  FI2(
    "fi2.ctb", R.string.louis_ttd_FI2
  ),

  FI_FI(
    "fi-fi.ctb", R.string.louis_ttd_FI_FI
  ),

  FI_FI_8DOT(
    "fi-fi-8dot.ctb", R.string.louis_ttd_FI_FI_8DOT
  ),

  FR_BFU_COMP6(
    "fr-bfu-comp6.utb", R.string.louis_ttd_FR_BFU_COMP6
  ),

  FR_BFU_COMP8(
    "fr-bfu-comp8.utb", R.string.louis_ttd_FR_BFU_COMP8
  ),

  FR_BFU_G2(
    "fr-bfu-g2.ctb", R.string.louis_ttd_FR_BFU_G2
  ),

  GA_G2(
    "ga-g2.ctb", R.string.louis_ttd_GA_G2
  ),

  GD(
    "gd.ctb", R.string.louis_ttd_GD
  ),

  GON(
    "gon.ctb", R.string.louis_ttd_GON
  ),

  GRC_INTL_EN(
    "grc-international-en.utb", R.string.louis_ttd_GRC_INTL_EN
  ),

  HAW_US_G1(
    "haw-us-g1.ctb", R.string.louis_ttd_HAW_US_G1
  ),

  HE(
    "he.ctb", R.string.louis_ttd_HE
  ),

  HR_G1(
    "hr-g1.ctb", R.string.louis_ttd_HR_G1
  ),

  HU_HU_COMP8(
    "hu-hu-comp8.ctb", R.string.louis_ttd_HU_HU_COMP8
  ),

  HU_HU_G1(
    "hu-hu-g1.ctb", R.string.louis_ttd_HU_HU_G1
  ),

  HY(
    "hy.ctb", R.string.louis_ttd_HY
  ),

  IS(
    "is.ctb", R.string.louis_ttd_IS
  ),

  IU_CA_G1(
    "iu-ca-g1.ctb", R.string.louis_ttd_IU_CA_G1
  ),

  KO_2006_G1(
    "ko-2006-g1.ctb", R.string.louis_ttd_KO_2006_G1
  ),

  KO_2006_G2(
    "ko-2006-g2.ctb", R.string.louis_ttd_KO_2006_G2
  ),

  KO_G1(
    "ko-g1.ctb", R.string.louis_ttd_KO_G1
  ),

  KO_G2(
    "ko-g2.ctb", R.string.louis_ttd_KO_G2
  ),

  KOK(
    "kok.ctb", R.string.louis_ttd_KOK
  ),

  KRU(
    "kru.ctb", R.string.louis_ttd_KRU
  ),

  LT(
    "lt.ctb", R.string.louis_ttd_LT
  ),

  MAO_NZ_G1(
    "mao-nz-g1.ctb", R.string.louis_ttd_MAO_NZ_G1
  ),

  MT(
    "mt.ctb", R.string.louis_ttd_MT
  ),

  MUN(
    "mun.ctb", R.string.louis_ttd_MUN
  ),

  MWR(
    "mwr.ctb", R.string.louis_ttd_MWR
  ),

  NE(
    "ne.ctb", R.string.louis_ttd_NE
  ),

  NL_BE_G0(
    "nl-BE-g0.utb", R.string.louis_ttd_NL_BE_G0
  ),

  NL_NL_G0(
    "nl-NL-g0.utb", R.string.louis_ttd_NL_NL_G0
  ),

  NO_NO_COMP8(
    "no-no-comp8.ctb", R.string.louis_ttd_NO_NO_COMP8
  ),

  NO_NO_G1(
    "no-no-g1.ctb", R.string.louis_ttd_NO_NO_G1
  ),

  NO_NO_G2(
    "no-no-g2.ctb", R.string.louis_ttd_NO_NO_G2
  ),

  NO_NO_G3(
    "no-no-g3.ctb", R.string.louis_ttd_NO_NO_G3
  ),

  NO_NO_GENERIC(
    "no-no-generic.ctb", R.string.louis_ttd_NO_NO_GENERIC
  ),

  PI(
    "pi.ctb", R.string.louis_ttd_PI
  ),

  PL_PL_COMP8(
    "pl-pl-comp8.ctb", R.string.louis_ttd_PL_PL_COMP8
  ),

  PT_PT_COMP8(
    "pt-pt-comp8.ctb", R.string.louis_ttd_PT_PT_COMP8
  ),

  PT_PT_G2(
    "pt-pt-g2.ctb", R.string.louis_ttd_PT_PT_G2
  ),

  RO(
    "ro.ctb", R.string.louis_ttd_RO
  ),

  RU(
    "ru.ctb", R.string.louis_ttd_RU
  ),

  RU_COMPBRL(
    "ru-compbrl.ctb", R.string.louis_ttd_RU_COMPBRL
  ),

  RU_LITBRL(
    "ru-litbrl.ctb", R.string.louis_ttd_RU_LITBRL
  ),

  SE_SE(
    "se-se.ctb", R.string.louis_ttd_SE_SE
  ),

  SL_SI_COMP8(
    "sl-si-comp8.ctb", R.string.louis_ttd_SL_SI_COMP8
  ),

  SOT_ZA_G1(
    "sot-za-g1.ctb", R.string.louis_ttd_SOT_ZA_G1
  ),

  SR_G1(
    "sr-g1.ctb", R.string.louis_ttd_SR_G1
  ),

  SV_1989(
    "sv-1989.ctb", R.string.louis_ttd_SV_1989
  ),

  SV_1996(
    "sv-1996.ctb", R.string.louis_ttd_SV_1996
  ),

  TA(
    "ta.ctb", R.string.louis_ttd_TA
  ),

  TA_TA_G1(
    "ta-ta-g1.ctb", R.string.louis_ttd_TA_TA_G1
  ),

  TR(
    "tr.ctb", R.string.louis_ttd_TR
  ),

  TSN_ZA_G1(
    "tsn-za-g1.ctb", R.string.louis_ttd_TSN_ZA_G1
  ),

  VI(
    "vi.ctb", R.string.louis_ttd_VI
  ),

  VI_G1(
    "vi-g1.ctb", R.string.louis_ttd_VI_G1
  ),

  ZH_HK(
    "zh-hk.ctb", R.string.louis_ttd_ZH_HK
  ),

  ZH_TW(
    "zh-tw.ctb", R.string.louis_ttd_ZH_TW
  ),

  PINYIN(R.string.louis_ttd_PINYIN),
  ; // end of enumeration

  private final String forwardTableName;
  private final String backwardTableName;
  private final int translatorDescription;
  private Translator translatorObject = null;

  TranslatorIdentifier (String forwardName, String backwardName, int description) {
    forwardTableName = forwardName;
    backwardTableName = backwardName;
    translatorDescription = description;
  }

  TranslatorIdentifier (String name, int description) {
    this(name, name, description);
  }

  TranslatorIdentifier (int description) {
    this(null, description);
  }

  public final String getDescription () {
    return Louis.getContext().getString(translatorDescription);
  }

  private final static Set<TranslatorIdentifier> auxiliaryTranslators =
         new LinkedHashSet<TranslatorIdentifier>();

  private static int currentAuxiliaryTranslatorsCounter = 0;
  private int myAuxiliaryTranslatorsCounter = 0;

  public final static boolean addAuxiliaryTranslator (TranslatorIdentifier identifier) {
    synchronized (auxiliaryTranslators) {
      boolean added = auxiliaryTranslators.add(identifier);
      currentAuxiliaryTranslatorsCounter += 1;
      return added;
    }
  }

  public final static boolean removeAuxiliaryTranslator (TranslatorIdentifier identifier) {
    synchronized (auxiliaryTranslators) {
      boolean removed = auxiliaryTranslators.remove(identifier);
      currentAuxiliaryTranslatorsCounter += 1;
      return removed;
    }
  }

  private static interface TableNameGetter {
    public String getTableName (TranslatorIdentifier identifier);
  }

  private final String makeTableList (TableNameGetter tableNameGetter) {
    StringBuilder sb = new StringBuilder();
    sb.append(tableNameGetter.getTableName(this));

    for (TranslatorIdentifier identifier : auxiliaryTranslators) {
      sb.append(InternalTable.TABLE_LIST_DELIMITER);
      sb.append(tableNameGetter.getTableName(identifier));
    }

    return sb.toString();
  }

  public final Translator getTranslator () {
    synchronized (this) {
      synchronized (auxiliaryTranslators) {
        if (myAuxiliaryTranslatorsCounter != currentAuxiliaryTranslatorsCounter) {
          translatorObject = null;
          myAuxiliaryTranslatorsCounter = currentAuxiliaryTranslatorsCounter;
        }

        if (translatorObject == null) {
          switch (this) {
            case PINYIN:
              translatorObject = new PinYinTranslator();
              break;

            default:
              translatorObject = new InternalTranslator(
                makeTableList(
                  new TableNameGetter() {
                    @Override
                    public String getTableName (TranslatorIdentifier identifier) {
                      return identifier.forwardTableName;
                    }
                  }
                ),

                makeTableList(
                  new TableNameGetter() {
                    @Override
                    public String getTableName (TranslatorIdentifier identifier) {
                      return identifier.backwardTableName;
                    }
                  }
                )
              );
              break;
          }
        }
      }
    }

    return translatorObject;
  }
}
