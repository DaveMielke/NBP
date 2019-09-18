package org.nbp.b2g.ui;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.Stack;

import org.nbp.common.LazyInstantiator;

import org.nbp.b2g.ui.host.HostEndpoint;
import org.nbp.b2g.ui.popup.PopupEndpoint;
import org.nbp.b2g.ui.prompt.FindEndpoint;
import org.nbp.b2g.ui.prompt.UnicodeEndpoint;
import org.nbp.b2g.ui.remote.RemoteEndpoint;

public abstract class Endpoints {
  public final static LazyInstantiator<HostEndpoint> host = new
    LazyInstantiator<HostEndpoint>(HostEndpoint.class);

  public final static LazyInstantiator<PopupEndpoint> popup = new
    LazyInstantiator<PopupEndpoint>(PopupEndpoint.class);

  public final static LazyInstantiator<FindEndpoint> find = new
    LazyInstantiator<FindEndpoint>(FindEndpoint.class);

  private final static LazyInstantiator<UnicodeEndpoint> unicode = new
    LazyInstantiator<UnicodeEndpoint>(UnicodeEndpoint.class);

  public final static LazyInstantiator<RemoteEndpoint> remote = new
    LazyInstantiator<RemoteEndpoint>(RemoteEndpoint.class);

  private final static ReadWriteLock CURRENT_ENDPOINT_LOCK = new ReentrantReadWriteLock();
  public final static Lock READ_LOCK = CURRENT_ENDPOINT_LOCK.readLock();
  private final static Lock WRITE_LOCK = CURRENT_ENDPOINT_LOCK.writeLock();

  private static Endpoint currentEndpoint = null;
  private final static Stack<Endpoint> endpointStack = new Stack<Endpoint>();

  public static Endpoint getCurrentEndpoint () {
    READ_LOCK.lock();
    try {
      return currentEndpoint;
    } finally {
      READ_LOCK.unlock();
    }
  }

  public final static Endpoint getPreviousEndpoint () {
    READ_LOCK.lock();
    try {
      return endpointStack.peek();
    } finally {
      READ_LOCK.unlock();
    }
  }

  public static boolean setCurrentEndpoint (Endpoint endpoint) {
    WRITE_LOCK.lock();
    try {
      boolean prepare = true;

      if (endpoint == null) {
        if (endpointStack.empty()) return false;
        endpoint = endpointStack.pop();
        prepare = false;
      } else if (endpoint == host.get()) {
        endpointStack.clear();
      } else if (endpointStack.contains(endpoint)) {
        return false;
      } else if (endpoint == currentEndpoint) {
        return false;
      } else {
        endpointStack.push(currentEndpoint);
      }

      if (endpoint != currentEndpoint) {
        if (currentEndpoint != null) currentEndpoint.onBackground();
        currentEndpoint = endpoint;
        if (currentEndpoint != null) currentEndpoint.onForeground(prepare);
      }
    } finally {
      WRITE_LOCK.unlock();
    }

    return true;
  }

  public final static boolean setPreviousEndpoint () {
    return setCurrentEndpoint(null);
  }

  public static boolean setHostEndpoint () {
    return setCurrentEndpoint(host.get());
  }

  public static boolean setPopupEndpoint (CharSequence text, int first, PopupClickHandler handler) {
    return setCurrentEndpoint(popup.get().set(text, first, handler));
  }

  public static boolean setPopupEndpoint (CharSequence text, PopupClickHandler handler) {
    return setCurrentEndpoint(popup.get().set(text, handler));
  }

  public static boolean setPopupEndpoint (CharSequence text) {
    return setCurrentEndpoint(popup.get().set(text));
  }

  public static boolean pushPopupEndpoint (CharSequence text, int first, PopupClickHandler handler) {
    return setCurrentEndpoint(new PopupEndpoint().set(text, first, handler));
  }

  public static boolean pushPopupEndpoint (CharSequence text, PopupClickHandler handler) {
    return setCurrentEndpoint(new PopupEndpoint().set(text, handler));
  }

  public static boolean pushPopupEndpoint (CharSequence text) {
    return setCurrentEndpoint(new PopupEndpoint().set(text));
  }

  public static boolean setFindEndpoint (boolean backward) {
    FindEndpoint endpoint = find.get();

    synchronized (endpoint) {
      if (!setCurrentEndpoint(endpoint)) return false;;
      return true;
    }
  }

  public static boolean setUnicodeEndpoint () {
    return setCurrentEndpoint(unicode.get());
  }

  public static boolean setRemoteEndpoint () {
    return setCurrentEndpoint(remote.get());
  }

  private Endpoints () {
  }

  static {
    setHostEndpoint();
  }
}
