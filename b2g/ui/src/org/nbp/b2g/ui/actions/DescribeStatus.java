package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.content.Context;
import android.os.Bundle;
import android.os.BatteryManager;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

public class DescribeStatus extends Action {
  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    sb.append(ApplicationContext.getString(label));
    sb.append(": ");
  }

  private static void addBatteryStatus (StringBuilder sb) {
    Bundle battery = HostMonitor.getBatteryStatus();

    if (battery != null) {
      startLine(sb, R.string.describeStatus_label_battery);

      {
        int level = battery.getInt(BatteryManager.EXTRA_LEVEL);
        int scale = battery.getInt(BatteryManager.EXTRA_SCALE);

        if (scale > 0) {
          sb.append(' ');
          sb.append(Integer.toString((level * 100) / scale));
          sb.append('%');
        }
      }
    }
  }

  private static void addWifiStatus (StringBuilder sb) {
    WifiManager wifi = (WifiManager)ApplicationContext.getSystemService(Context.WIFI_SERVICE);

    if (wifi != null) {
      WifiInfo info = wifi.getConnectionInfo();

      if (info != null) {
        startLine(sb, R.string.describeStatus_label_wifi);
        sb.append(info.getSSID());

        int dbm = info.getRssi();
        sb.append(' ');
        sb.append(wifi.calculateSignalLevel(dbm, 100));
        sb.append('%');

        sb.append(' ');
        sb.append(info.getLinkSpeed());
        sb.append("Mbps");
      }
    }
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();

    addBatteryStatus(sb);
    addWifiStatus(sb);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeStatus (Endpoint endpoint) {
    super(endpoint, false);
  }
}
