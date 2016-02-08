package org.nbp.common;

import java.util.Map;
import java.util.LinkedHashMap;

public class CachingMap<K, V> extends LinkedHashMap<K, V> {
  private final int cacheSize;

  @Override
  protected boolean removeEldestEntry (Entry entry) {
    return size() > cacheSize;
  }

  public CachingMap (int size) {
    super((((size * 4) / 3) + 2), 1f, true);
    cacheSize = size;
  }
}
