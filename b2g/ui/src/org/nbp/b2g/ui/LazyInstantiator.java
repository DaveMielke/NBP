package org.nbp.b2g.ui;

import android.util.Log;

public class LazyInstantiator<T> {
  private final static String LOG_TAG = LazyInstantiator.class.getName();

  private final Class<T> objectType;
  private Object objectReference = null;
  private final static Object objectLock = new Object();

  public T get () {
    synchronized (objectLock) {
      if (objectReference == null) {
        try {
          objectReference = objectType.newInstance();
        } catch (InstantiationException exception) {
          Log.w(LOG_TAG, "object not instantiated: " + exception.getMessage());
        } catch (IllegalAccessException exception) {
          Log.w(LOG_TAG, "object not accessible: " + exception.getMessage());
        }
      }
    }

    return (T)objectReference;
  }

  public LazyInstantiator (Class<T> type) {
    objectType = type;
  }
}
