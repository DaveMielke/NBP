package org.nbp.b2g.ui;

import java.util.Map;
import org.nbp.common.CacheMap;
import org.liblouis.Translation;

public abstract class TranslationCache {
  private final static int CACHE_SIZE = 5;

  private final static Map<String, Translation> map =
    new CacheMap<String, Translation>(CACHE_SIZE);

  public static void clear () {
    synchronized (map) {
      map.clear();
    }
  }

  public static void put (CharSequence text, Translation translation) {
    if (text.length() > 0) {
      synchronized (map) {
        map.put(text.toString(), translation);
      }
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
