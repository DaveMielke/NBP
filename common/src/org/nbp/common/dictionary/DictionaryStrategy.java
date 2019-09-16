package org.nbp.common.dictionary;

public enum DictionaryStrategy {
  DEFAULT(".", "x"),
  EXACT("exact", "match headwords exactly"),
  PREFIX("prefix", "match prefixes"),
  SUBSTRING("substring", "match substring occurring anywhere in a headword"),
  SUFFIX("suffix", "match suffixes"),
  REGEXP_EXTENDED("re", "POSIX 1003.2 (modern) regular expressions"),
  REGEX_BASIC("regexp", "old (basic) regular expressions"),
  SOUNDEX("soundex", "match using SOUNDEX algorithm"),
  LEVENSHTEIN1("lev", "match headwords within Levenshtein distance one"),
  WORD("word", "match separate words within headwords"),
  FIRST("first", "match the first word within headwords"),
  LAST("last", "match the last word within headwords"),
  ; // end of enumeration

  private final String strategyName;
  private final String strategyDescription;

  DictionaryStrategy (String name, String description) {
    strategyName = name;
    strategyDescription = description;
  }

  public final String getName () {
    return strategyName;
  }

  public final String getDescription () {
    return strategyDescription;
  }
}
