package org.liblouis;

import java.util.Set;
import java.util.LinkedHashSet;

public enum TranslatorIdentifier {
  BRAILLE_PATTERNS("braille-patterns.cti", R.string.louis_ttd_BRAILLE_PATTERNS),
  IPA_SAFE("IPA-unicode-range.uti", R.string.louis_ttd_IPA_SAFE),
  IPA_ALL("IPA.utb", R.string.louis_ttd_IPA_ALL),
  BOXES("boxes.ctb", R.string.louis_ttd_BOXES),
  EN_CHESS("en-chess.ctb", R.string.louis_ttd_EN_CHESS),

  AFR("afr-za-g1.ctb"),
  BG_COMP8("bg.tbl"),
  BH("bh.tbl"),
  BO_COMP8("bo.tbl"),
  CA("ca.tbl"),
  CHR("chr-us-g1.ctb"),
  CKB("ckb.tbl"),
  CS("cs.tbl"),
  CS_COMP8("cs-comp8.utb"),
  CY_G1("cy-cy-g1.utb"),
  CY_G2("cy.tbl"),
  DA_G1("da-dk-g16.ctb"),
  DA_G15("da-dk-g26l.ctb"),
  DA_G2("da-dk-g26.ctb"),
  DA_COMP8("da-dk-g08.ctb"),
  DE_G0("de-g0.utb"),
  DE_G1("de-g1.ctb"),
  DE_G2("de-g2.ctb"),
  DE_COMP8("de-de-comp8.ctb"),
  DRA_COMP8("dra.tbl"),
  EL("el.ctb"),
  EN_CA_COMP8("en_CA.tbl"),
  EN_GB_G1("en-gb-g1.utb"),
  EN_GB_G2("en_GB.tbl"),
  EN_GB_COMP8("en-gb-comp8.ctb"),
  EN_UEB_G1("en-ueb-g1.ctb"),
  EN_UEB_G2("en-ueb-g2.ctb"),
  EN_US_G1("en-us-g1.ctb"),
  EN_US_G2("en_US.tbl"),
  EN_US_COMP6("en-us-comp6.ctb"),
  EN_US_COMP8("en_US-comp8-ext.tbl"),
  EO("eo.tbl"),
  EO_X("eo-g1-x-system.ctb"),
  ES_G1("es.tbl"),
  ES_G2("es-g2.ctb"),
  ET_COMP8("et.tbl"),
  FI("fi.utb"),
  FI_COMP8("fi-fi-8dot.ctb"),
  FR_G1("fr-bfu-comp6.utb"),
  FR_G2("fr-bfu-g2.ctb"),
  FR_COMP8("fr-bfu-comp8.utb"),
  GA_G1("ga-g1.utb"),
  GA_G2("ga.tbl"),
  GD_COMP8("gd.tbl"),
  GON("gon.tbl"),
  GRC_INTL_EN("grc-international-en.utb"),
  HAW("haw-us-g1.ctb"),
  HE_COMP8("he.tbl"),
  HR("hr-g1.tbl"),
  HR_COMP8("hr-comp8.tbl"),
  HU_G1("hu.tbl"),
  HU_G2("hu-hu-g2.ctb"),
  HU_COMP8("hu-hu-comp8.ctb"),
  HY_COMP8("hy.tbl"),
  IS("is.tbl"),
  IU("iu-ca-g1.ctb"),
  KO_G1("ko-g1.ctb"),
  KO_G2("ko-g2.ctb"),
  KO_2006_G1("ko-2006-g1.ctb"),
  KO_2006_G2("ko-2006-g2.ctb"),
  KOK("kok.tbl"),
  KRU("kru.tbl"),
  LT("lt-6dot.tbl"),
  MAO("mao-nz-g1.ctb"),
  MT_COMP8("mt.tbl"),
  MUN("mun.tbl"),
  MWR("mwr.tbl"),
  NE("ne.tbl"),
  NL_BE("nl_BE.tbl"),
  NL_NL("nl.tbl"),
  NO_G0("no-no-g0.utb"),
  NO_G1("no-no-g1.ctb"),
  NO_G2("no-no-g2.ctb"),
  NO_G3("no.tbl"),
  NO_COMP8("no-no-comp8.ctb"),
  PI("pi.tbl"),
  PL("pl.tbl"),
  PL_COMP8("pl-pl-comp8.ctb"),
  PT_G1("pt-pt-g1.utb"),
  PT_G2("pt.tbl"),
  PT_COMP8("pt-pt-comp8.ctb"),
  RO_COMP8("ro.tbl"),
  RU("ru.tbl"),
  RU_COMP8("ru-compbrl.ctb"),
  SL("sl.tbl"),
  SL_COMP8("sl-si-comp8.ctb"),
  SR("sr.tbl"),
  SV("sv.tbl"),
  SV_COMP8_1989("sv-1989.ctb"),
  SV_COMP8_1996("sv-1996.ctb"),
  TA("ta-ta-g1.ctb"),
  TA_COMP8("ta.tbl"),
  TR_COMP8("tr.tbl"),
  VI("vi.tbl"),
  VI_COMP8("vi.ctb"),
  ZH_HK("zh_HK.tbl"),
  ZH_TW("zh_TW.tbl"),

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
