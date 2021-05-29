package org.nbp.duxbury;

public enum DuxburyTranslators {
  PINYIN(R.string.duxbury_ttd_PINYIN),
  ; // end of enumeration

  private final String forwardTableName;
  private final String backwardTableName;
  private final int translatorDescription;
  private Translator translatorObject = null;

  DuxburyTranslators (String forwardName, String backwardName, int description) {
    forwardTableName = forwardName;
    backwardTableName = backwardName;
    translatorDescription = description;
  }

  DuxburyTranslators (String name, int description) {
    this(name, name, description);
  }

  DuxburyTranslators (int description) {
    this(null, description);
  }

  DuxburyTranslators (String forwardName, String backwardName) {
    this(forwardName, backwardName, 0);
  }

  DuxburyTranslators (String name) {
    this(name, name);
  }
}
