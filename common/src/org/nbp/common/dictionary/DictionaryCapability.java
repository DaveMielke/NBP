package org.nbp.common.dictionary;

public enum DictionaryCapability {
  MIME("mime", "the OPTION MIME command is supported"),
  AUTH("auth", "the AUTH command is supported"),
  KERBEROS_V4("kerberos_v4", "the SASL Kerberos version 4 mechanism is supported"),
  GSSAPI("gssapi", "the SASL GSSAPI [RFC2078] mechanism is supported"),
  SKEY("skey", "the SASL S/Key [RFC1760] mechanism is supported"),
  EXTERNAL("external", "the SASL external mechanism is supported"),
  ; // end of enumeration

  private final String capabilityName;
  private final String capabilityDescription;

  DictionaryCapability (String name, String description) {
    capabilityName = name;
    capabilityDescription = description;
  }

  public final String getName () {
    return capabilityName;
  }

  public final String getDescription () {
    return capabilityDescription;
  }
}
