package org.nbp.common.dictionary;

public enum DictionaryDatabase {
  ALL("*", "all of the dictionaries"),
  FIRST("!", "the first dictionary that contains a match"),

  GCIDE("gcide", "The Collaborative International Dictionary of English v.0.48"),
  WORD_NET("wn", "WordNet (r) 3.0 (2006)"),
  THESAURUS_MOBY("moby-thesaurus", "Moby Thesaurus II by Grady Ward, 1.0"),
  ACRONYMS_VERA("vera", "V.E.R.A. -- Virtual Entity of Relevant Acronyms (September 2014)"),
  JARGON("jargon", "The Jargon File (version 4.4.7, 29 Dec 2003)"),

  ELEMENTS("elements", "The Elements (07Nov00)"),
  FOLDOC("foldoc", "The Free On-line Dictionary of Computing (18 March 2015)"),
  WORLD_2002("world02", "CIA World Factbook 2002"),

  BOUVIER("bouvier", "Bouvier's Law Dictionary, Revised 6th Ed (1856)"),
  EASTON("easton", "Easton's 1897 Bible Dictionary"),
  HITCHCOCK("hitchcock", "Hitchcock's Bible Names Dictionary (late 1800's)"),

  GAZ2K_PLACES("gaz2k-places", "U.S. Gazetteer Places (2000)"),
  GAZ2K_COUNTIES("gaz2k-counties", "U.S. Gazetteer Counties (2000)"),
  GAZ2K_ZIPS("gaz2k-zips", "U.S. Gazetteer Zip Code Tabulation Areas (2000)"),

  FD_AFRIKAANS_GERMAN("fd-afr-deu", "Afrikaans-German (FreeDict)"),
  FD_CROATIAN_ENGLISH("fd-cro-eng", "Croatian-English (FreeDict)"),
  FD_CZECH_ENGLISH("fd-cze-eng", "Czech-English (FreeDict)"),
  FD_DANISH_ENGLISH("fd-dan-eng", "Danish-English (FreeDict)"),
  FD_DUTCH_ENGLISH("fd-nld-eng", "Dutch-English (FreeDict)"),
  FD_DUTCH_FRENCH("fd-nld-fra", "Dutch-French (FreeDict)"),
  FD_DUTCH_GERMAN("fd-nld-deu", "Dutch-German (FreeDict)"),
  FD_ENGLISH_ARABIC("fd-eng-ara", "English-Arabic (FreeDict)"),
  FD_ENGLISH_CROATIAN("fd-eng-cro", "English-Croatian (FreeDict)"),
  FD_ENGLISH_CZECH("fd-eng-cze", "English-Czech (FreeDict)"),
  FD_ENGLISH_DUTCH("fd-eng-nld", "English-Dutch (FreeDict)"),
  FD_ENGLISH_FRENCH("fd-eng-fra", "English-French (FreeDict)"),
  FD_ENGLISH_GERMAN("fd-eng-deu", "English-German (FreeDict)"),
  FD_ENGLISH_HINDI("fd-eng-hin", "English-Hindi (FreeDict)"),
  FD_ENGLISH_HUNGARIAN("fd-eng-hun", "English-Hungarian (FreeDict)"),
  FD_ENGLISH_IRISH("fd-eng-iri", "English-Irish (FreeDict)"),
  FD_ENGLISH_ITALIAN("fd-eng-ita", "English-Italian (FreeDict)"),
  FD_ENGLISH_LATIN("fd-eng-lat", "English-Latin (FreeDict)"),
  FD_ENGLISH_PORTUGUESE("fd-eng-por", "English-Portuguese (FreeDict)"),
  FD_ENGLISH_ROMANIAN("fd-eng-rom", "English-Romanian (FreeDict)"),
  FD_ENGLISH_RUSSIAN("fd-eng-rus", "English-Russian (FreeDict)"),
  FD_ENGLISH_SCR("fd-eng-scr", "English-Serbo-Croat (FreeDict)"),
  FD_ENGLISH_SPANISH("fd-eng-spa", "English-Spanish (FreeDict)"),
  FD_ENGLISH_SWAHILI("fd-eng-swa", "English-Swahili (FreeDict)"),
  FD_ENGLISH_SWEDISH("fd-eng-swe", "English-Swedish (FreeDict)"),
  FD_ENGLISH_TURKISH("fd-eng-tur", "English-Turkish (FreeDict)"),
  FD_ENGLISH_WELSH("fd-eng-wel", "English-Welsh (FreeDict)"),
  FD_FRENCH_DUTCH("fd-fra-nld", "French-Dutch (FreeDict)"),
  FD_FRENCH_ENGLISH("fd-fra-eng", "French-English (FreeDict)"),
  FD_FRENCH_GERMAN("fd-fra-deu", "French-German (FreeDict)"),
  FD_GERMAN_DUTCH("fd-deu-nld", "German-Dutch (FreeDict)"),
  FD_GERMAN_ENGLISH("fd-deu-eng", "German-English (FreeDict)"),
  FD_GERMAN_FRENCH("fd-deu-fra", "German-French (FreeDict)"),
  FD_GERMAN_ITALIAN("fd-deu-ita", "German-Italian (FreeDict)"),
  FD_GERMAN_PORTUGUESE("fd-deu-por", "German-Portuguese (FreeDict)"),
  FD_HUNGARIAN_ENGLISH("fd-hun-eng", "Hungarian-English (FreeDict)"),
  FD_IRISH_ENGLISH("fd-iri-eng", "Irish-English (FreeDict)"),
  FD_ITALIAN_ENGLISH("fd-ita-eng", "Italian-English (FreeDict)"),
  FD_ITALIAN_GERMAN("fd-ita-deu", "Italian-German (FreeDict)"),
  FD_JAPANESE_GERMAN("fd-jpn-deu", "Japanese-German (FreeDict)"),
  FD_LATIN_ENGLISH("fd-lat-eng", "Latin-English (FreeDict)"),
  FD_LATIN_GERMAN("fd-lat-deu", "Latin-German (FreeDict)"),
  FD_PORTUGUESE_ENGLISH("fd-por-eng", "Portuguese-English (FreeDict)"),
  FD_PORTUGUESE_GERMAN("fd-por-deu", "Portuguese-German (FreeDict)"),
  FD_SCOTISH_GERMAN("fd-gla-deu", "Scottish-Gaelic-German (FreeDict)"),
  FD_SCR_ENGLISH("fd-scr-eng", "Serbo-Croat-English (FreeDict)"),
  FD_SLOVAK_ENGLISH("fd-slo-eng", "Slovak-English (FreeDict)"),
  FD_SPANISH_ENGLISH("fd-spa-eng", "Spanish-English (FreeDict)"),
  FD_SWAHILI_ENGLISH("fd-swa-eng", "Swahili-English (FreeDict)"),
  FD_SWEDISH_ENGLISH("fd-swe-eng", "Swedish-English (FreeDict)"),
  FD_TURKISH_ENGLISH("fd-tur-eng", "Turkish-English (FreeDict)"),
  FD_TURKISH_GERMAN("fd-tur-deu", "Turkish-German (FreeDict)"),
  FD_WELSH_ENGLISH("fd-wel-eng", "Welsh-English (FreeDict)"),
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
