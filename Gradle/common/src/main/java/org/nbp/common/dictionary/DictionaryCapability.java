package org.nbp.common.dictionary;

public enum DictionaryCapability {
  MIME("mime", "the OPTION MIME command"),
  AUTHORIZATION("auth", "the AUTH command"),
  KERBEROS_V4("kerberos_v4", "the SASL Kerberos version 4 mechanism"),
  GSSAPI("gssapi", "the SASL GSSAPI mechanism [RFC2078]"),
  SKEY("skey", "the SASL S/Key mechanism [RFC1760]"),
  EXTERNAL("external", "the SASL external mechanism"),
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
