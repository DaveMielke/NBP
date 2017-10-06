package org.liblouis;

public enum EmphasisEnumeration {
  BOLD("bold", Emphasis.getBoldBit()),
  ITALIC("italic", Emphasis.getItalicBit()),
  UNDERLINE("underline", Emphasis.getUnderlineBit()),
  ;

  private final String className;
  private final short defaultBit;
  public final static short NO_BIT = 0;

  public final String getClassName () {
    return className;
  }

  public final short getDefaultBit () {
    return defaultBit;
  }

  private EmphasisEnumeration (String name, short bit) {
    className = name;
    defaultBit = bit;
  }

  private EmphasisEnumeration (String name) {
    this(name, NO_BIT);
  }
}
