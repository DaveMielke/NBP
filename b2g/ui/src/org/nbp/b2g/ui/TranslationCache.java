package org.nbp.b2g.ui;

import java.util.LinkedHashMap;
import org.liblouis.Translation;

public class TranslationCache extends LinkedHashMap<String, Translation> {
  private final static int CACHE_SIZE = 5;
  private final static float LOAD_FACTOR = 0.75f;

  @Override
  protected boolean removeEldestEntry (Entry entry) {
    return size() > CACHE_SIZE;
  }

  public TranslationCache () {
    super(CACHE_SIZE+1, LOAD_FACTOR, true);
  }
}
