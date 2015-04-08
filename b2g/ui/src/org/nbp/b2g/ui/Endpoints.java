package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;
import org.nbp.b2g.ui.bluetooth.BluetoothEndpoint;

public class Endpoints {
  public final static Object LOCK = new Object();

  private final static HostEndpoint hostEndpoint = new HostEndpoint();
  private final static BluetoothEndpoint bluetoothEndpoint = new BluetoothEndpoint();
  private static Endpoint currentEndpoint = hostEndpoint;

  public static Endpoint getCurrentEndpoint () {
    synchronized (LOCK) {
      return currentEndpoint;
    }
  }

  public static HostEndpoint getHostEndpoint () {
    return hostEndpoint;
  }

  private Endpoints () {
  }
}
