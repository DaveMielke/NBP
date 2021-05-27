package org.nbp.common;

import android.util.Log;

public class LazyInstantiator<T> {
  private final static String LOG_TAG = LazyInstantiator.class.getName();

  private final Class<? extends T> objectType;
  private T objectReference = null;

  public final boolean isInstantiated () {
    return objectReference != null;
  }

  public final T get () {
    synchronized (this) {
      if (!isInstantiated()) {
        try {
          objectReference = objectType.newInstance();
        } catch (InstantiationException exception) {
          throw new RuntimeException(exception);
        } catch (IllegalAccessException exception) {
          throw new RuntimeException(exception);
        }
      }
    }

    return objectReference;
  }

  public LazyInstantiator (Class<? extends T> type) {
    objectType = type;
  }
}
