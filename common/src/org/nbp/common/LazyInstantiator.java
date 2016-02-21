package org.nbp.common;

import android.util.Log;

public class LazyInstantiator<T> {
  private final static String LOG_TAG = LazyInstantiator.class.getName();

  private final Class<T> objectType;
  private T objectReference = null;

  public T get () {
    synchronized (this) {
      if (objectReference == null) {
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

  public LazyInstantiator (Class<T> type) {
    objectType = type;
  }
}
