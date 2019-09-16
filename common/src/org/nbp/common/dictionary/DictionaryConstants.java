package org.nbp.common.dictionary;

public abstract class DictionaryConstants {
  private DictionaryConstants () {
  }

  public final static String DATABASE_ALL   = "*";
  public final static String DATABASE_FIRST = "!";

  public final static String STRATEGY_DEFAULT = ".";
  public final static String STRATEGY_EXACT = "exact"; // match headwords exactly
  public final static String STRATEGY_PREFIX = "prefix"; // match prefixes
  public final static String STRATEGY_SUBSTRING = "substring"; // match substring occurring anywhere in a headword
  public final static String STRATEGY_SUFFIX = "suffix"; // match suffixes
  public final static String STRATEGY_REGEXP_EXTENDED = "re"; // POSIX 1003.2 (modern) regular expressions
  public final static String STRATEGY_REGEX_BASIC = "regexp"; // old (basic) regular expressions
  public final static String STRATEGY_SOUNDEX = "soundex"; // match using SOUNDEX algorithm
  public final static String STRATEGY_LEVENSHTEIN1 = "lev"; // match headwords within Levenshtein distance one
  public final static String STRATEGY_WORD = "word"; // match separate words within headwords
  public final static String STRATEGY_FIRST = "first"; // match the first word within headwords
  public final static String STRATEGY_LAST = "last"; // match the last word within headwords

  public final static String CAPABILITY_MIME = "mime"; // the OPTION MIME command is supported
  public final static String CAPABILITY_AUTH = "auth"; // the AUTH command is supported
  public final static String CAPABILITY_KERBEROS_V4 = "kerberos_v4"; // the SASL Kerberos version 4 mechanism is supported
  public final static String CAPABILITY_GSSAPI = "gssapi"; // the SASL GSSAPI [RFC2078] mechanism is supported
  public final static String CAPABILITY_SKEY = "skey"; // the SASL S/Key [RFC1760] mechanism is supported
  public final static String CAPABILITY_EXTERNAL = "external"; // the SASL external mechanism is supported
}
