package org.liblouis;

import java.util.Set;
import java.util.LinkedHashSet;

public enum InternalTranslatorIdentifier implements TranslatorIdentifier {
  BRAILLE_PATTERNS("braille-patterns.cti", R.string.louis_ttd_BRAILLE_PATTERNS),
  IPA_SAFE("IPA-unicode-range.uti", R.string.louis_ttd_IPA_SAFE),
  IPA_ALL("IPA.utb", R.string.louis_ttd_IPA_ALL),
  BOXES("boxes.ctb", R.string.louis_ttd_BOXES),
  EN_CHESS("en-chess.ctb", R.string.louis_ttd_EN_CHESS),

  AFR_G1("afr-za-g1.ctb"),
  AFR_G2("afr-za-g2.ctb"),
  AR_G1("ar.tbl"),
  AR_G2("ar-ar-g2.ctb"),
  AR_COMP8("ar-ar-comp8.utb"),
  AS("as.tbl"),
  AWA("awa.tbl"),
  BG_COMP8("bg.tbl"),
  BH("bh.tbl"),
  BN("bn.tbl"),
  BO_COMP8("bo.tbl"),
  BRA("bra.tbl"),
  CA("ca.tbl"),
  CHR("chr-us-g1.ctb"),
  CKB("ckb.tbl"),
  CS("cs.tbl"),
  CS_COMP8("cs-comp8.utb"),
  CY_G1("cy-cy-g1.utb"),
  CY_G2("cy.tbl"),
  DA_G1("da-dk-g16.ctb"),
  DA_G1_FORWARD("da-dk-g16-lit.ctb"),
  DA_G1_8DOT("da-dk-g18.ctb"),
  DA_G15("da-dk-g26l.ctb"),
  DA_G15_FORWARD("da-dk-g26l-lit.ctb"),
  DA_G15_8DOT("da-dk-g28l.ctb"),
  DA_G2("da-dk-g26.ctb"),
  DA_G2_FORWARD("da-dk-g26-lit.ctb"),
  DA_G2_8DOT("da-dk-g28.ctb"),
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
  EN_NABCC("en-nabcc.utb"),
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
  ES_COMP8("Es-Es-G0.utb"),
  ET_COMP8("et.tbl"),
  FA("fa-ir-g1.utb"),
  FA_COMP8("fa-ir-comp8.ctb"),
  FI("fi.utb"),
  FI_COMP8("fi-fi-8dot.ctb"),
  FR_G1("fr-bfu-comp6.utb"),
  FR_G2("fr-bfu-g2.ctb"),
  FR_COMP8("fr-bfu-comp8.utb"),
  GA_G1("ga-g1.utb"),
  GA_G2("ga-g2.ctb"),
  GD_COMP8("gd.tbl"),
  GEZ("gez.tbl"),
  GON("gon.tbl"),
  GRC_INTL_EN("grc-international-en.utb"),
  GU("gu.tbl"),
  HAW("haw-us-g1.ctb"),
  HE_COMP8("he.tbl"),
  HI("hi.tbl"),
  HR("hr-g1.tbl"),
  HR_COMP8("hr-comp8.tbl"),
  HU_G1("hu.tbl"),
  HU_G2("hu-hu-g2.ctb"),
  HU_COMP8("hu-hu-comp8.ctb"),
  HY_COMP8("hy.tbl"),
  IS("is.tbl"),
  IT("it.tbl"),
  IT_COMP8("it-it-comp8.utb"),
  IU("iu-ca-g1.ctb"),
  KHA("kha.tbl"),
  KN("kn.tbl"),
  KO_G1("ko-g1.ctb"),
  KO_G2("ko-g2.ctb"),
  KO_2006_G1("ko-2006-g1.ctb"),
  KO_2006_G2("ko-2006-g2.ctb"),
  KOK("kok.tbl"),
  KRU("kru.tbl"),
  LT("lt-6dot.tbl"),
  LT_8DOT("lt.tbl"),
  LV("lv.tbl"),
  MAO("mao-nz-g1.ctb"),
  ML("ml.tbl"),
  MN_G1("mn-MN-g1.utb"),
  MN_G2("mn-MN-g2.ctb"),
  MNI("mni.tbl"),
  MR("mr.tbl"),
  MT_COMP8("mt.tbl"),
  MUN("mun.tbl"),
  MWR("mwr.tbl"),
  NE("ne.tbl"),
  NL_BE("nl_BE.tbl"),
  NL_NL("nl.tbl"),
  NO_G0("no-no-g0.utb"),
  NO_G0_8DOT("no-no-8dot.utb"),
  NO_G0_8DOT_6DOT("no-no-8dot-fallback-6dot-g0.utb"),
  NO_G1("no-no-g1.ctb"),
  NO_G2("no-no-g2.ctb"),
  NO_G3("no.tbl"),
  NO_COMP8("no-no-comp8.ctb"),
  OR("or.tbl"),
  PA("pa.tbl"),
  PI("pi.tbl"),
  PL("pl.tbl"),
  PL_COMP8("pl-pl-comp8.ctb"),
  PT_G1("pt-pt-g1.utb"),
  PT_G2("pt.tbl"),
  PT_COMP8("pt-pt-comp8.ctb"),
  RO_COMP8("ro.tbl"),
  RU("ru.tbl"),
  RU_COMP8("ru-compbrl.ctb"),
  SA("sa.tbl"),
  SD("sd.tbl"),
  SK("sk.tbl"),
  SL("sl.tbl"),
  SL_COMP8("sl-si-comp8.ctb"),
  SR("sr.tbl"),
  SV("sv.tbl"),
  SV_COMP8_1989("sv-1989.ctb"),
  SV_COMP8_1996("sv-1996.ctb"),
  TA("ta-ta-g1.ctb"),
  TA_COMP8("ta.tbl"),
  TE("te.tbl"),
  TR_8DOT("tr-g2.tbl"),
  TR_COMP8("tr.tbl"),
  UK("uk.utb"),
  UR_G1("ur-pk-g1.utb"),
  UR_G2("ur-pk-g2.ctb"),
  VI("vi.tbl"),
  VI_COMP8("vi.ctb"),
  ZH_CN_NOTONES("zh_CHN.tbl"),
  ZH_CN_TONES("zhcn-g1.ctb"),
  ZH_CN_2CELL("zhcn-g2.ctb"),
  ZH_HK("zh_HK.tbl"),
  ZH_TW("zh_TW.tbl"),
  ; // end of enumeration

  private final String forwardTableName;
  private final String backwardTableName;
  private final int translatorDescription;
  private Translator translatorObject = null;

  InternalTranslatorIdentifier (String forwardName, String backwardName, int description) {
    forwardTableName = forwardName;
    backwardTableName = backwardName;
    translatorDescription = description;
  }

  InternalTranslatorIdentifier (String name, int description) {
    this(name, name, description);
  }

  InternalTranslatorIdentifier (int description) {
    this(null, description);
  }

  InternalTranslatorIdentifier (String forwardName, String backwardName) {
    this(forwardName, backwardName, 0);
  }

  InternalTranslatorIdentifier (String name) {
    this(name, name);
  }

  @Override
  public final String getName () {
    return name();
  }

  @Override
  public final String getDescription () {
    if (translatorDescription != 0) {
      return Louis.getContext().getString(translatorDescription);
    }

    String table = forwardTableName;

    if (table != null) {
      String description = Metadata.getValueForKey(table, "index-name");
      if (description != null) return description;

      description = Metadata.getValueForKey(table, "display-name");
      if (description != null) return description;
    }

    return null;
  }

  private final static Set<InternalTranslatorIdentifier> auxiliaryTranslators =
         new LinkedHashSet<InternalTranslatorIdentifier>();

  private static int currentAuxiliaryTranslatorsCounter = 0;
  private int myAuxiliaryTranslatorsCounter = 0;

  public final static boolean addAuxiliaryTranslator (InternalTranslatorIdentifier identifier) {
    synchronized (auxiliaryTranslators) {
      boolean added = auxiliaryTranslators.add(identifier);
      currentAuxiliaryTranslatorsCounter += 1;
      return added;
    }
  }

  public final static boolean removeAuxiliaryTranslator (InternalTranslatorIdentifier identifier) {
    synchronized (auxiliaryTranslators) {
      boolean removed = auxiliaryTranslators.remove(identifier);
      currentAuxiliaryTranslatorsCounter += 1;
      return removed;
    }
  }

  private static interface TableNameGetter {
    public String getTableName (InternalTranslatorIdentifier identifier);
  }

  private final String makeTableList (TableNameGetter tableNameGetter) {
    StringBuilder sb = new StringBuilder();
    sb.append(tableNameGetter.getTableName(this));

    for (InternalTranslatorIdentifier identifier : auxiliaryTranslators) {
      sb.append(InternalTable.TABLE_LIST_DELIMITER);
      sb.append(tableNameGetter.getTableName(identifier));
    }

    return sb.toString();
  }

  @Override
  public final Translator getTranslator () {
    synchronized (this) {
      synchronized (auxiliaryTranslators) {
        if (myAuxiliaryTranslatorsCounter != currentAuxiliaryTranslatorsCounter) {
          translatorObject = null;
          myAuxiliaryTranslatorsCounter = currentAuxiliaryTranslatorsCounter;
        }

        if (translatorObject == null) {
          translatorObject = new InternalTranslator(
            makeTableList(
              new TableNameGetter() {
                @Override
                public String getTableName (InternalTranslatorIdentifier identifier) {
                  return identifier.forwardTableName;
                }
              }
            ),

            makeTableList(
              new TableNameGetter() {
                @Override
                public String getTableName (InternalTranslatorIdentifier identifier) {
                  return identifier.backwardTableName;
                }
              }
            )
          );
        }
      }
    }

    return translatorObject;
  }
}
