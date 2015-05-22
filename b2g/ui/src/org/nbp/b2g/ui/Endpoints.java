package org.nbp.b2g.ui;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.nbp.b2g.ui.host.HostEndpoint;
import org.nbp.b2g.ui.prompt.FindEndpoint;
import org.nbp.b2g.ui.bluetooth.BluetoothEndpoint;

public abstract class Endpoints {
  private final static ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
  public final static Lock READ_LOCK = READ_WRITE_LOCK.readLock();
  private final static Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();
  private static Endpoint currentEndpoint = null;

  public static Endpoint getCurrentEndpoint () {
    READ_LOCK.lock();
    try {
      return currentEndpoint;
    } finally {
      READ_LOCK.unlock();
    }
  }

  private static void setCurrentEndpoint (Endpoint endpoint) {
    WRITE_LOCK.lock();
    try {
      if (endpoint != currentEndpoint) {
        if (currentEndpoint != null) currentEndpoint.onBackground();
        currentEndpoint = endpoint;
        if (currentEndpoint != null) currentEndpoint.onForeground();
      }
    } finally {
      WRITE_LOCK.unlock();
    }
  }

  private final static HostEndpoint hostEndpoint = new HostEndpoint();
  private final static FindEndpoint findEndpoint = new FindEndpoint();
  private final static BluetoothEndpoint bluetoothEndpoint = new BluetoothEndpoint();

  public static HostEndpoint getHostEndpoint () {
    return hostEndpoint;
  }

  public static void setHostEndpoint () {
    setCurrentEndpoint(hostEndpoint);
  }

  public static void setFindEndpoint () {
    setCurrentEndpoint(findEndpoint);
  }

  public static void setBluetoothEndpoint () {
    setCurrentEndpoint(bluetoothEndpoint);
  }

  private Endpoints () {
  }

  static {
    setHostEndpoint();
  }
}
