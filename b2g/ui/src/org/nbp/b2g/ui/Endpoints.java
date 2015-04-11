package org.nbp.b2g.ui;

import org.nbp.b2g.ui.host.HostEndpoint;
import org.nbp.b2g.ui.find.FindEndpoint;
import org.nbp.b2g.ui.bluetooth.BluetoothEndpoint;

public class Endpoints {
  public final static Object LOCK = new Object();
  private static Endpoint currentEndpoint = null;

  public static Endpoint getCurrentEndpoint () {
    synchronized (LOCK) {
      return currentEndpoint;
    }
  }

  private static void setCurrentEndpoint (Endpoint endpoint) {
    synchronized (LOCK) {
      if (endpoint != currentEndpoint) {
        if (currentEndpoint != null) currentEndpoint.onBackground();
        currentEndpoint = endpoint;
        if (currentEndpoint != null) currentEndpoint.onForeground();
      }
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
