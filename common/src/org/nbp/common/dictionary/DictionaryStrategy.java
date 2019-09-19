package org.nbp.common.dictionary;

public enum DictionaryStrategy {
  DEFAULT(".", "the server's preferred algorithm for spelling correction"),
  EXACT("exact", "exact match"),
  PREFIX("prefix", "match the prefix"),

  SUBSTRING("substring", "match any substring"),
  SUFFIX("suffix", "match the suffix"),
  REGEX_POSIX("re", "extended regular expression [POSIX 1003.2]"),
  REGEX_BASIC("regexp", "basic regular expression"),
  SOUNDEX("soundex", "the SOUNDEX algorithm [KNUTH73]"),
  LEVENSTEIN1("lev", "Levenstein distance one [PZ85]"),
  WORD_ANY("word", "match any subword"),
  WORD_FIRST("first", "match the first subword"),
  WORD_LAST("last", "match the last subword"),
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
