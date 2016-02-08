package org.nbp.common;

import java.util.Map;
import java.util.LinkedHashMap;

public class CacheMap<K, V> extends LinkedHashMap<K, V> {
  private final int cacheSize;

  @Override
  protected boolean removeEldestEntry (Entry entry) {
    return size() > cacheSize;
  }

  public CacheMap (int size) {
    super(size*2, 1f, true);
    cacheSize = size;
  }
}
