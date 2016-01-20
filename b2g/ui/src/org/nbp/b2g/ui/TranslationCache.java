package org.nbp.b2g.ui;

import java.util.Map;
import java.util.LinkedHashMap;
import org.liblouis.Translation;

public abstract class TranslationCache {
  private final static int CACHE_SIZE = 5;
  private final static float LOAD_FACTOR = 1f;

  private final static Map<String, Translation> map =
  new LinkedHashMap<String, Translation>(
    CACHE_SIZE*2, LOAD_FACTOR, true
  ) {
    @Override
    protected boolean removeEldestEntry (Entry entry) {
      return size() > CACHE_SIZE;
    }
  };

  public static void clear () {
    synchronized (map) {
      map.clear();
    }
  }

  public static void put (CharSequence text, Translation translation) {
    synchronized (map) {
      map.put(text.toString(), translation);
    }
  }

  public static Translation get (CharSequence text) {
    synchronized (map) {
      return map.get(text.toString());
    }
  }

  private TranslationCache () {
  }
}
