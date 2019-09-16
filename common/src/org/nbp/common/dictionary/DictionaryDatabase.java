package org.nbp.common.dictionary;

public enum DictionaryDatabase {
  ALL("*", "all of the dictionaries"),
  FIRST("!", "the first dictionary that contains a match"),

  GCIDE("gcide", "The Collaborative International Dictionary of English v.0.48"),
  WN("wn", "WordNet (r) 3.0 (2006)"),
  THESAURUS_MOBY("moby-thesaurus", "Moby Thesaurus II by Grady Ward, 1.0"),
  ELEMENTS("elements", "The Elements (07Nov00)"),
  VERA("vera", "V.E.R.A. -- Virtual Entity of Relevant Acronyms (September 2014)"),
  JARGON("jargon", "The Jargon File (version 4.4.7, 29 Dec 2003)"),
  FOLDOC("foldoc", "The Free On-line Dictionary of Computing (18 March 2015)"),
  EASTON("easton", "Easton's 1897 Bible Dictionary"),
  HITCHCOCK("hitchcock", "Hitchcock's Bible Names Dictionary (late 1800's)"),
  BOUVIER("bouvier", "Bouvier's Law Dictionary, Revised 6th Ed (1856)"),
  WORLD02("world02", "CIA World Factbook 2002"),
  GAZ2K_COUNTIES("gaz2k-counties", "U.S. Gazetteer Counties (2000)"),
  GAZ2K_PLACES("gaz2k-places", "U.S. Gazetteer Places (2000)"),
  GAZ2K_ZIPS("gaz2k-zips", "U.S. Gazetteer Zip Code Tabulation Areas (2000)"),
  FD_TUR_ENG("fd-tur-eng", "Turkish-English FreeDict Dictionary ver. 0.2.1"),
  FD_POR_DEU("fd-por-deu", "Portuguese-German FreeDict Dictionary ver. 0.1.1"),
  FD_NLD_ENG("fd-nld-eng", "Dutch-English Freedict Dictionary ver. 0.1.3"),
  FD_ENG_ARA("fd-eng-ara", "English-Arabic FreeDict Dictionary ver. 0.6.2"),
  FD_SPA_ENG("fd-spa-eng", "Spanish-English FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_HUN("fd-eng-hun", "English-Hungarian FreeDict Dictionary ver. 0.1"),
  FD_ITA_ENG("fd-ita-eng", "Italian-English FreeDict Dictionary ver. 0.1.1"),
  FD_WEL_ENG("fd-wel-eng", "Welsh-English Freedict dictionary"),
  FD_ENG_NLD("fd-eng-nld", "English-Dutch FreeDict Dictionary ver. 0.1.1"),
  FD_FRA_ENG("fd-fra-eng", "French-English FreeDict Dictionary ver. 0.3.4"),
  FD_TUR_DEU("fd-tur-deu", "Turkish-German FreeDict Dictionary ver. 0.1.1"),
  FD_SWE_ENG("fd-swe-eng", "Swedish-English FreeDict Dictionary ver. 0.1.1"),
  FD_NLD_FRA("fd-nld-fra", "Nederlands-French FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_SWA("fd-eng-swa", "English-Swahili xFried/FreeDict Dictionary"),
  FD_DEU_NLD("fd-deu-nld", "German-Dutch FreeDict Dictionary ver. 0.1.1"),
  FD_FRA_DEU("fd-fra-deu", "French-German FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_CRO("fd-eng-cro", "English-Croatian Freedict Dictionary"),
  FD_ENG_ITA("fd-eng-ita", "English-Italian FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_LAT("fd-eng-lat", "English-Latin FreeDict Dictionary ver. 0.1.1"),
  FD_LAT_ENG("fd-lat-eng", "Latin-English FreeDict Dictionary ver. 0.1.1"),
  FD_FRA_NLD("fd-fra-nld", "French-Dutch FreeDict Dictionary ver. 0.1.2"),
  FD_ITA_DEU("fd-ita-deu", "Italian-German FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_HIN("fd-eng-hin", "English-Hindi FreeDict Dictionary ver. 1.5.1"),
  FD_DEU_ENG("fd-deu-eng", "German-English FreeDict Dictionary ver. 0.3.4"),
  FD_POR_ENG("fd-por-eng", "Portuguese-English FreeDict Dictionary ver. 0.1.1"),
  FD_LAT_DEU("fd-lat-deu", "Latin - German FreeDict dictionary ver. 0.4"),
  FD_JPN_DEU("fd-jpn-deu", "Japanese-German FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_DEU("fd-eng-deu", "English-German FreeDict Dictionary ver. 0.3.6"),
  FD_ENG_SCR("fd-eng-scr", "English-Serbo-Croat Freedict dictionary"),
  FD_ENG_ROM("fd-eng-rom", "English-Romanian FreeDict Dictionary ver. 0.6.1"),
  FD_IRI_ENG("fd-iri-eng", "Irish-English Freedict dictionary"),
  FD_CZE_ENG("fd-cze-eng", "Czech-English Freedict dictionary"),
  FD_SCR_ENG("fd-scr-eng", "Serbo-Croat-English Freedict dictionary"),
  FD_ENG_CZE("fd-eng-cze", "English-Czech fdicts/FreeDict Dictionary"),
  FD_ENG_RUS("fd-eng-rus", "English-Russian FreeDict Dictionary ver. 0.3"),
  FD_AFR_DEU("fd-afr-deu", "Afrikaans-German FreeDict Dictionary ver. 0.3"),
  FD_ENG_POR("fd-eng-por", "English-Portuguese FreeDict Dictionary ver. 0.2.2"),
  FD_HUN_ENG("fd-hun-eng", "Hungarian-English FreeDict Dictionary ver. 0.3.1"),
  FD_ENG_SWE("fd-eng-swe", "English-Swedish FreeDict Dictionary ver. 0.1.1"),
  FD_DEU_ITA("fd-deu-ita", "German-Italian FreeDict Dictionary ver. 0.1.1"),
  FD_CRO_ENG("fd-cro-eng", "Croatian-English Freedict Dictionary"),
  FD_DAN_ENG("fd-dan-eng", "Danish-English FreeDict Dictionary ver. 0.2.1"),
  FD_ENG_TUR("fd-eng-tur", "English-Turkish FreeDict Dictionary ver. 0.2.1"),
  FD_ENG_SPA("fd-eng-spa", "English-Spanish FreeDict Dictionary ver. 0.2.1"),
  FD_NLD_DEU("fd-nld-deu", "Dutch-German FreeDict Dictionary ver. 0.1.1"),
  FD_DEU_POR("fd-deu-por", "German-Portuguese FreeDict Dictionary ver. 0.2.1"),
  FD_SWA_ENG("fd-swa-eng", "Swahili-English xFried/FreeDict Dictionary"),
  FD_HIN_ENG("fd-hin-eng", "English-Hindi Freedict Dictionary [reverse index]"),
  FD_DEU_FRA("fd-deu-fra", "German-French FreeDict Dictionary ver. 0.3.1"),
  FD_ENG_FRA("fd-eng-fra", "English-French FreeDict Dictionary ver. 0.1.4"),
  FD_SLO_ENG("fd-slo-eng", "Slovak-English Freedict dictionary"),
  FD_GLA_DEU("fd-gla-deu", "Scottish Gaelic-German FreeDict Dictionary ver. 0.1.1"),
  FD_ENG_WEL("fd-eng-wel", "English-Welsh Freedict dictionary"),
  FD_ENG_IRI("fd-eng-iri", "English-Irish Freedict dictionary"),
  ; // end of enumeration

  private final String databaseName;
  private final String databaseDescription;

  DictionaryDatabase (String name, String description) {
    databaseName = name;
    databaseDescription = description;
  }

  public final String getName () {
    return databaseName;
  }

  public final String getDescription () {
    return databaseDescription;
  }
}
