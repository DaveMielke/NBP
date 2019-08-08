package org.liblouis;

import java.util.Set;
import java.util.LinkedHashSet;

public enum TranslatorIdentifier {
  BRAILLE_PATTERNS("braille-patterns.cti", R.string.louis_ttd_BRAILLE_PATTERNS),
  IPA_SAFE("IPA-unicode-range.uti", R.string.louis_ttd_IPA_SAFE),
  IPA_ALL("IPA.utb", R.string.louis_ttd_IPA_ALL),
  BOXES("boxes.ctb", R.string.louis_ttd_BOXES),
  EN_CHESS("en-chess.ctb", R.string.louis_ttd_EN_CHESS),

  AFR_ZA_G1("afr-za-g1.ctb"),
  BG("bg.ctb"),
  BH("bh.ctb"),
  BO("bo.ctb"),
  CA_G1("ca-g1.ctb"),
  CHR_US_G1("chr-us-g1.ctb"),
  CKB_G1("ckb-g1.ctb"),
  CS_G1("cs-g1.ctb"),
  CY_CY_G2("cy-cy-g2.ctb"),
  DA_DK_G26("da-dk-g26.ctb"),
  DA_DK_G28("da-dk-g28.ctb"),
  DA_LT("da-lt.ctb"),
  DE_DE_COMP8("de-de-comp8.ctb"),
  DE_G1("de-g1.ctb"),
  DE_G2("de-g2.ctb"),
  DRA("dra.ctb"),
  EL("el.ctb"),
  EN_CA("en_CA.ctb"),
  EN_GB_COMP8("en-gb-comp8.ctb"),
  EN_GB_G2("en-GB-g2.ctb"),
  EN_IN_G1("en-in-g1.ctb"),
  EN_UEB_G1("en-ueb-g1.ctb"),
  EN_UEB_G2("en-ueb-g2.ctb"),
  EN_US_COMP6("en-us-comp6.ctb"),
  EN_US_COMP8("en-us-comp8.ctb"),
  EN_US_G1("en-us-g1.ctb"),
  EN_US_G2("en_US.tbl"),
  EO_G1("eo.tbl"),
  EO_G1_X_SYSTEM("eo-g1-x-system.ctb"),
  ES_G1("es.tbl"),
  ES_G2("es-g2.ctb"),
  ET("et.ctb"),
  ETHIO_G1("ethio-g1.ctb"),
  FI1("fi1.ctb"),
  FI2("fi2.ctb"),
  FI_FI("fi-fi.ctb"),
  FI_FI_8DOT("fi-fi-8dot.ctb"),
  FR_BFU_COMP6("fr-bfu-comp6.utb"),
  FR_BFU_COMP8("fr-bfu-comp8.utb"),
  FR_BFU_G2("fr-bfu-g2.ctb"),
  GA_G2("ga-g2.ctb"),
  GD("gd.ctb"),
  GON("gon.ctb"),
  GRC_INTL_EN("grc-international-en.utb"),
  HAW_US_G1("haw-us-g1.ctb"),
  HE("he.tbl"),
  HR_G1("hr-g1.ctb"),
  HU_HU_COMP8("hu-hu-comp8.ctb"),
  HU_HU_G1("hu-hu-g1.ctb"),
  HY("hy.ctb"),
  IS("is.ctb"),
  IU_CA_G1("iu-ca-g1.ctb"),
  KO_2006_G1("ko-2006-g1.ctb"),
  KO_2006_G2("ko-2006-g2.ctb"),
  KO_G1("ko-g1.ctb"),
  KO_G2("ko-g2.ctb"),
  KOK("kok.ctb"),
  KRU("kru.ctb"),
  LT("lt.ctb"),
  MAO_NZ_G1("mao-nz-g1.ctb"),
  MT("mt.ctb"),
  MUN("mun.ctb"),
  MWR("mwr.ctb"),
  NE("ne.ctb"),
  NL_BE_G0("nl-BE-g0.utb"),
  NL_NL_G0("nl-NL-g0.utb"),
  NO_NO_COMP8("no-no-comp8.ctb"),
  NO_NO_G1("no-no-g1.ctb"),
  NO_NO_G2("no-no-g2.ctb"),
  NO_NO_G3("no-no-g3.ctb"),
  NO_NO_GENERIC("no-no-generic.ctb"),
  PI("pi.ctb"),
  PL_PL_COMP8("pl-pl-comp8.ctb"),
  PT_PT_COMP8("pt-pt-comp8.ctb"),
  PT_PT_G2("pt-pt-g2.ctb"),
  RO("ro.ctb"),
  RU("ru.ctb"),
  RU_COMPBRL("ru-compbrl.ctb"),
  RU_LITBRL("ru-litbrl.ctb"),
  SE_SE("se-se.ctb"),
  SL_SI_COMP8("sl-si-comp8.ctb"),
  SOT_ZA_G1("sot-za-g1.ctb"),
  SR_G1("sr-g1.ctb"),
  SV_1989("sv-1989.ctb"),
  SV_1996("sv-1996.ctb"),
  TA("ta.ctb"),
  TA_TA_G1("ta-ta-g1.ctb"),
  TR("tr.ctb"),
  TSN_ZA_G1("tsn-za-g1.ctb"),
  VI("vi.ctb"),
  VI_G1("vi-g1.ctb"),
  ZH_HK("zh-hk.ctb"),
  ZH_TW("zh-tw.ctb"),

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

  TranslatorIdentifier (String forwardName, String backwardName) {
    this(forwardName, backwardName, 0);
  }

  TranslatorIdentifier (String name) {
    this(name, name);
  }

  public final String getDescription () {
    String table = forwardTableName;

    if (table != null) {
      String description = Metadata.getValueForKey(table, "display-name");
      if (description != null) return description;
    }

    if (translatorDescription == 0) return null;
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
