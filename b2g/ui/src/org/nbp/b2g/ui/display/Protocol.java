package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import android.util.Log;
import android.os.Build;
import android.bluetooth.BluetoothAdapter;

public abstract class Protocol extends Component {
  private final static String LOG_TAG = Protocol.class.getName();

  protected Protocol (DisplayEndpoint endpoint) {
    super(endpoint);
  }

  private static Integer cellCount = null;
  protected final static int getCellCount () {
    if (cellCount == null) cellCount = Devices.braille.get().getLength();
    return cellCount;
  }

  protected final boolean write (byte[] cells) {
    return write(Braille.toString(cells));
  }

  protected final boolean write (byte[] buffer, int from, int count) {
    if ((from == 0) && (count == buffer.length)) return write(buffer);

    byte[] cells = new byte[count];
    System.arraycopy(buffer, from, cells, 0, count);
    return write(cells);
  }

  protected final boolean write (byte[] buffer, int from) {
    return write(buffer, from, getCellCount());
  }

  protected final Channel getChannel () {
    return displayEndpoint.getChannel();
  }

  protected final boolean flushOutput () {
    return getChannel().flush();
  }

  public void resetKeys () {
  }

  protected static void logIgnoredByte (byte b) {
    Log.w(LOG_TAG, String.format("input byte ignored: 0X%02X", b));
  }

  public void resetInput () {
  }

  public boolean handleTimeout () {
    return true;
  }

  public boolean handleInput (byte b) {
    logIgnoredByte(b);
    return true;
  }

  public int handleNavigationKeys (int keyMask, boolean press) {
    return keyMask;
  }

  public boolean handleCursorKey (int keyNumber, boolean press) {
    return false;
  }

  protected final String getString (String string, int width) {
    if (string == null) return null;

    int length = string.length();
    if (length == width) return string;
    if (length > width) return string.substring(length-width);

    StringBuilder sb = new StringBuilder(string);
    while (sb.length() < width) sb.append(' ');
    return sb.toString();
  }

  protected final String getSerialNumber () {
    String serialNumber = Build.SERIAL;
    if (serialNumber == null) return null;
    if (serialNumber.equals(Build.UNKNOWN)) return null;
    return serialNumber;
  }

  protected final String getSerialNumber (int width) {
    return getString(getSerialNumber(), width);
  }

  protected final String getBluetoothName () {
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    if (adapter == null) return null;
    return adapter.getName();
  }

  protected final String getBluetoothName (int width) {
    return getString(getBluetoothName(), width);
  }
}
