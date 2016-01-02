package org.liblouis;

import java.io.File;
import java.io.FileFilter;

public enum TranslationTable {
  AFR_ZA_G1(
    "afr-za-g1",
    "Afrikaans"
  ),

  BG(
    "bg",
    "Bulgarian"
  ),

  BH(
    "bh",
    "Bihari"
  ),

  BO(
    "bo",
    "Tibetan"
  ),

  BOXES(
    "boxes",
    "(box drawing characters)"
  ),

  CA_G1(
    "ca-g1",
    "Catalan"
  ),

  CHR_US_G1(
    "chr-us-g1",
    "Cherokee"
  ),

  CKB_G1(
    "ckb-g1",
    "Sorani (Kurdish)"
  ),

  CS_G1(
    "cs-g1",
    "Czech"
  ),

  CY_CY_G2(
    "cy-cy-g2",
    "Welsh, Grade 2"
  ),

  DA_DK_G26(
    "da-dk-g26",
    "Danish, Grade 2 (six dots)"
  ),

  DA_DK_G28(
    "da-dk-g28",
    "Danish, Grade 2 (eight dots)"
  ),

  DA_LT(
    "da-lt",
    "Danish, LogText (ISO-8859-1)"
  ),

  DE_CHESS(
    "de-chess",
    "Chess, German"
  ),

  DE_CH_G1(
    "de-ch-g1",
    "Swiss German, Grade 1"
  ),

  DE_CH_G2(
    "de-ch-g2",
    "Swiss German, Grade 2"
  ),

  DE_DE_COMP8(
    "de-de-comp8",
    "German (eight-dot computer braille)"
  ),

  DE_DE_G1(
    "de-de-g1",
    "German, Grade 1"
  ),

  DE_DE_G2(
    "de-de-g2",
    "German, Grade 2"
  ),

  DRA(
    "dra",
    "Dravidian"
  ),

  EN_CA(
    "en_CA",
    "English, Canada"
  ),

  EN_CHESS(
    "en-chess",
    "Chess, English"
  ),

  EN_GB_COMP8(
    "en-gb-comp8",
    "English, UK (eight-dot computer braille)"
  ),

  EN_GB_G2(
    "en-GB-g2",
    "English, UK Grade 2"
  ),

  EN_IN_G1(
    "en-in-g1",
    "English, India"
  ),

  EN_UEB_G1(
    "en-ueb-g1",
    "English, Unified, Grade 1"
  ),

  EN_UEB_G2(
    "en-ueb-g2",
    "English, Unified, Grade 2"
  ),

  EN_US_COMP6(
    "en-us-comp6",
    "English, US (six-dot computer braille)"
  ),

  EN_US_COMP8(
    "en-us-comp8",
    "English, US (eight-dot computer braille)"
  ),

  EN_US_COMPBRL(
    "en-us-compbrl",
    "English, US (computer braille)"
  ),

  EN_US_G1(
    "en-us-g1",
    "English, US, Grade 1"
  ),

  EN_US_G2(
    "en-us-g2",
    "English, US, Grade 2"
  ),

  EN_US_INTERLINE(
    "en-us-interline",
    "English, US, Grade 1"
  ),

  EN_US_MATHTEXT(
    "en-us-mathtext",
    "English, US, Grade 1"
  ),

  EO_G1(
    "eo-g1",
    "Esperanto"
  ),

  EO_G1_X_SYSTEM(
    "eo-g1-x-system",
    "Esperanto (X System)"
  ),

  ES_G1(
    "es-g1",
    "Spanish"
  ),

  ET(
    "et",
    "Estonian"
  ),

  ETHIO_G1(
    "ethio-g1",
    "Ethiopic, Grade 1"
  ),

  FI1(
    "fi1",
    "Finnish (eight dots, similar to six dots)"
  ),

  FI2(
    "fi2",
    "Finnish (eight dots, similar to six dots, enhanced)"
  ),

  FI_FI_8DOT(
    "fi-fi-8dot",
    "Finnish (eight dots)"
  ),

  FI_FI(
    "fi-fi",
    "Finnish (eight dots)"
  ),

  FR_2007(
    "fr-2007",
    "French, Unified (2007)"
  ),

  FR_BFU_G2(
    "fr-bfu-g2",
    "French, Unified, Grade 2"
  ),

  FR_CA_G2(
    "Fr-Ca-g2",
    "French, Canada, Grade 2"
  ),

  FR_FR_G2(
    "Fr-Fr-g2",
    "French, France, Grade 2"
  ),

  GA_G2(
    "ga-g2",
    "Irish, Grade 2"
  ),

  GD(
    "gd",
    "Gaelic"
  ),

  GON(
    "gon",
    "Gondi"
  ),

  GR_BB(
    "gr-bb",
    "(Septuagint and Greek New Testament)"
  ),

  HAW_US_G1(
    "haw-us-g1",
    "Hawaiian"
  ),

  HE(
    "he",
    "Hebrew"
  ),

  HR(
    "hr",
    "Croatian"
  ),

  HU_HU_COMP8(
    "hu-hu-comp8",
    "Hungarian (eight-dot computer braille)"
  ),

  HU_HU_G1(
    "hu-hu-g1",
    "Hungarian, Grade 1"
  ),

  HY(
    "hy",
    "Armenian"
  ),

  IS(
    "is",
    "Icelandic"
  ),

  IU_CA_G1(
    "iu-ca-g1",
    "Inuktitut"
  ),

  KO_2006_G1(
    "ko-2006-g1",
    "Korean, Grade 1 (2006)"
  ),

  KO_2006_G2(
    "ko-2006-g2",
    "Korean, Grade 2 (2006)"
  ),

  KO_G1(
    "ko-g1",
    "Korean, Grade 1"
  ),

  KO_G2(
    "ko-g2",
    "Korean, Grade 2"
  ),

  KOK(
    "kok",
    "Konkani"
  ),

  KRU(
    "kru",
    "Kurukh"
  ),

  LT(
    "lt",
    "Lituanian"
  ),

  MAO_NZ_G1(
    "mao-nz-g1",
    "Maori"
  ),

  MARBURG(
    "marburg",
    "Mathematics, Marburg"
  ),

  MARBURG_EDIT(
    "marburg_edit",
    "Mathematics, Marburg (for post-translation editing)"
  ),

  MT(
    "mt",
    "Maltese"
  ),

  MUN(
    "mun",
    "Munda"
  ),

  MWR(
    "mwr",
    "Marwari"
  ),

  NE(
    "ne",
    "Nepali"
  ),

  NEMETH(
    "nemeth",
    "Mathematics, Nemeth"
  ),

  NEMETH_EDIT(
    "nemeth_edit",
    "Mathematics, Nemeth (for post-translation editing)"
  ),

  NL_BE_G1(
    "nl-BE-g1",
    "Dutch, Belgium"
  ),

  NL_NL_G1(
    "nl-NL-g1",
    "Dutch, Netherlands"
  ),

  NO_NO(
    "no-no",
    "Norwegian (based on the Offentlig Utvalg for Blindeskrift [Public Commission for Braille] translation table for CP1252)"
  ),

  NO_NO_G1(
    "no-no-g1",
    "Norwegian, Level 1"
  ),

  NO_NO_G2(
    "no-no-g2",
    "Norwegian, Level 2"
  ),

  NO_NO_G3(
    "no-no-g3",
    "Norwegian, Level 3"
  ),

  NO_NO_GENERIC(
    "no-no-generic",
    "Norwegian (includes some unofficial character representations to accommodate multilingual usage)"
  ),

  PI(
    "pi",
    "Pali"
  ),

  PL_PL_COMP8(
    "pl-pl-comp8",
    "Polish (eight-dot computer braille)"
  ),

  PT_PT_COMP8(
    "pt-pt-comp8",
    "Portuguese (eight-dot computer braille)"
  ),

  PT_PT_G2(
    "pt-pt-g2",
    "Portuguese, Grade 2"
  ),

  RO(
    "ro",
    "Romanian"
  ),

  RU_COMPBRL(
    "ru-compbrl",
    "Russian (computer braille)"
  ),

  RU(
    "ru",
    "Russian"
  ),

  RU_LITBRL(
    "ru-litbrl",
    "Russian, Literary"
  ),

  SE_SE(
    "se-se",
    "Swedish (1996)"
  ),

  SL_SI_COMP8(
    "sl-si-comp8",
    "Slovenian (eight-dot computer braille)"
  ),

  SOT_ZA_G1(
    "sot-za-g1",
    "Sotho"
  ),

  SPACES(
    "spaces",
    "(space characters)"
  ),

  SR_G1(
    "sr-g1",
    "Serbian"
  ),

  SV_1989(
    "sv-1989",
    "Swedish (1989)"
  ),

  SV_1996(
    "sv-1996",
    "Swedish (1996)"
  ),

  TA(
    "ta",
    "Tamil"
  ),

  TA_TA_G1(
    "ta-ta-g1",
    "Tamil, Grade 1"
  ),

  TR(
    "tr",
    "Turkish"
  ),

  TSN_ZA_G1(
    "tsn-za-g1",
    "Tswana"
  ),

  UEBC_G2(
    "UEBC-g2",
    "English, Unified, Grade 2"
  ),

  UKMATHS(
    "ukmaths",
    "Mathematics, UK"
  ),

  UKMATHS_EDIT(
    "ukmaths_edit",
    "Mathematics, UK (for post-translation editing)"
  ),

  VI(
    "vi",
    "Vietnamese"
  ),

  VI_G1(
    "vi-g1",
    "Vietnamese, Grade 1"
  ),

  WISKUNDE(
    "wiskunde",
    "Mathematics, Flemish (Woluwe)"
  ),

  ZH_HK(
    "zh-hk",
    "Chinese, Hong Kong, Cantonese"
  ),

  ZH_TW(
    "zh-tw",
    "Chinese, Taiwan"
  ),

  ; // end of enumeration

  private final String tableName;
  private final String tableDescription;

  TranslationTable (String name, String description) {
    tableName = name;
    tableDescription = description;
  }

  public final String getName () {
    return tableName;
  }

  public final String getDescription () {
    return tableDescription;
  }

  public final static String SUBDIRECTORY = "liblouis/tables";
  public final static String EXTENSION = ".ctb";

  private final static Object STATIC_LOCK = new Object();
  private static File tablesDirectory = null;

  public static File getDirectory () {
    synchronized (STATIC_LOCK) {
      if (tablesDirectory == null) {
        tablesDirectory = new File(Louis.getDataDirectory(), SUBDIRECTORY);
      }
    }

    return tablesDirectory;
  }

  public static File[] getFiles () {
    return getDirectory().listFiles(
      new FileFilter() {
        @Override
        public boolean accept (File file) {
          return file.getName().endsWith(EXTENSION);
        }
      }
    );
  }

  private File tableFile = null;

  public final File getFile () {
    synchronized (this) {
      if (tableFile == null) {
        tableFile = new File(getDirectory(), (getName() + EXTENSION));
      }
    }

    return tableFile;
  }
}
