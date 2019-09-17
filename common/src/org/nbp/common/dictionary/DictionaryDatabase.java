package org.nbp.common.dictionary;

public enum DictionaryDatabase {
  ALL("*", "all of the dictionaries"),
  FIRST("!", "the first dictionary that contains a match"),

  GCIDE("gcide", "The Collaborative International Dictionary of English"),
  WORD_NET("wn", "WordNet"),
  THESAURUS_MOBY("moby-thesaurus", "Moby Thesaurus II by Grady Ward"),
  ACRONYMS_VERA("vera", "Virtual Entity of Relevant Acronyms"),
  JARGON("jargon", "The Jargon File"),

  ELEMENTS("elements", "The Elements"),
  FOLDOC("foldoc", "The Free On-line Dictionary of Computing"),
  WORLD_2002("world02", "CIA World Factbook 2002"),

  BOUVIER("bouvier", "Bouvier's Law Dictionary"),
  EASTON("easton", "Easton's Bible Dictionary"),
  HITCHCOCK("hitchcock", "Hitchcock's Bible Names Dictionary"),

  GAZ2K_PLACES("gaz2k-places", "U.S. Gazetteer Places (2000)"),
  GAZ2K_COUNTIES("gaz2k-counties", "U.S. Gazetteer Counties (2000)"),
  GAZ2K_ZIPS("gaz2k-zips", "U.S. Gazetteer Zip Code Tabulation Areas (2000)"),

  FD_AFRIKAANS_GERMAN("fd-afr-deu", "Afrikaans to German (FreeDict)"),
  FD_CROATIAN_ENGLISH("fd-cro-eng", "Croatian to English (FreeDict)"),
  FD_CZECH_ENGLISH("fd-cze-eng", "Czech to English (FreeDict)"),
  FD_DANISH_ENGLISH("fd-dan-eng", "Danish to English (FreeDict)"),
  FD_DUTCH_ENGLISH("fd-nld-eng", "Dutch to English (FreeDict)"),
  FD_DUTCH_FRENCH("fd-nld-fra", "Dutch to French (FreeDict)"),
  FD_DUTCH_GERMAN("fd-nld-deu", "Dutch to German (FreeDict)"),
  FD_ENGLISH_ARABIC("fd-eng-ara", "English to Arabic (FreeDict)"),
  FD_ENGLISH_CROATIAN("fd-eng-cro", "English to Croatian (FreeDict)"),
  FD_ENGLISH_CZECH("fd-eng-cze", "English to Czech (FreeDict)"),
  FD_ENGLISH_DUTCH("fd-eng-nld", "English to Dutch (FreeDict)"),
  FD_ENGLISH_FRENCH("fd-eng-fra", "English to French (FreeDict)"),
  FD_ENGLISH_GERMAN("fd-eng-deu", "English to German (FreeDict)"),
  FD_ENGLISH_HINDI("fd-eng-hin", "English to Hindi (FreeDict)"),
  FD_ENGLISH_HUNGARIAN("fd-eng-hun", "English to Hungarian (FreeDict)"),
  FD_ENGLISH_IRISH("fd-eng-iri", "English to Irish (FreeDict)"),
  FD_ENGLISH_ITALIAN("fd-eng-ita", "English to Italian (FreeDict)"),
  FD_ENGLISH_LATIN("fd-eng-lat", "English to Latin (FreeDict)"),
  FD_ENGLISH_PORTUGUESE("fd-eng-por", "English to Portuguese (FreeDict)"),
  FD_ENGLISH_ROMANIAN("fd-eng-rom", "English to Romanian (FreeDict)"),
  FD_ENGLISH_RUSSIAN("fd-eng-rus", "English to Russian (FreeDict)"),
  FD_ENGLISH_SERBOCROAT("fd-eng-scr", "English to Serbo-Croat (FreeDict)"),
  FD_ENGLISH_SPANISH("fd-eng-spa", "English to Spanish (FreeDict)"),
  FD_ENGLISH_SWAHILI("fd-eng-swa", "English to Swahili (FreeDict)"),
  FD_ENGLISH_SWEDISH("fd-eng-swe", "English to Swedish (FreeDict)"),
  FD_ENGLISH_TURKISH("fd-eng-tur", "English to Turkish (FreeDict)"),
  FD_ENGLISH_WELSH("fd-eng-wel", "English to Welsh (FreeDict)"),
  FD_FRENCH_DUTCH("fd-fra-nld", "French to Dutch (FreeDict)"),
  FD_FRENCH_ENGLISH("fd-fra-eng", "French to English (FreeDict)"),
  FD_FRENCH_GERMAN("fd-fra-deu", "French to German (FreeDict)"),
  FD_GERMAN_DUTCH("fd-deu-nld", "German to Dutch (FreeDict)"),
  FD_GERMAN_ENGLISH("fd-deu-eng", "German to English (FreeDict)"),
  FD_GERMAN_FRENCH("fd-deu-fra", "German to French (FreeDict)"),
  FD_GERMAN_ITALIAN("fd-deu-ita", "German to Italian (FreeDict)"),
  FD_GERMAN_PORTUGUESE("fd-deu-por", "German to Portuguese (FreeDict)"),
  FD_HUNGARIAN_ENGLISH("fd-hun-eng", "Hungarian to English (FreeDict)"),
  FD_IRISH_ENGLISH("fd-iri-eng", "Irish to English (FreeDict)"),
  FD_ITALIAN_ENGLISH("fd-ita-eng", "Italian to English (FreeDict)"),
  FD_ITALIAN_GERMAN("fd-ita-deu", "Italian to German (FreeDict)"),
  FD_JAPANESE_GERMAN("fd-jpn-deu", "Japanese to German (FreeDict)"),
  FD_LATIN_ENGLISH("fd-lat-eng", "Latin to English (FreeDict)"),
  FD_LATIN_GERMAN("fd-lat-deu", "Latin to German (FreeDict)"),
  FD_PORTUGUESE_ENGLISH("fd-por-eng", "Portuguese to English (FreeDict)"),
  FD_PORTUGUESE_GERMAN("fd-por-deu", "Portuguese to German (FreeDict)"),
  FD_SCOTTISH_GERMAN("fd-gla-deu", "Scottish to German (FreeDict)"),
  FD_SERBOCROAT_ENGLISH("fd-scr-eng", "Serbo-Croat to English (FreeDict)"),
  FD_SLOVAK_ENGLISH("fd-slo-eng", "Slovak to English (FreeDict)"),
  FD_SPANISH_ENGLISH("fd-spa-eng", "Spanish to English (FreeDict)"),
  FD_SWAHILI_ENGLISH("fd-swa-eng", "Swahili to English (FreeDict)"),
  FD_SWEDISH_ENGLISH("fd-swe-eng", "Swedish to English (FreeDict)"),
  FD_TURKISH_ENGLISH("fd-tur-eng", "Turkish to English (FreeDict)"),
  FD_TURKISH_GERMAN("fd-tur-deu", "Turkish to German (FreeDict)"),
  FD_WELSH_ENGLISH("fd-wel-eng", "Welsh to English (FreeDict)"),
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
