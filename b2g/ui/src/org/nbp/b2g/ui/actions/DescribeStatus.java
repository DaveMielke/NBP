package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.content.Context;
import android.os.Bundle;

import android.os.BatteryManager;

import android.telephony.TelephonyManager;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

public class DescribeStatus extends Action {
  private final static String LOG_TAG = DescribeStatus.class.getName();

  private static void appendString (StringBuilder sb, int string) {
    sb.append(ApplicationContext.getString(string));
  }

  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
    sb.append(": ");
  }

  private static void addBatteryStatus (StringBuilder sb) {
    Bundle battery = HostMonitor.getBatteryStatus();

    if (battery != null) {
      startLine(sb, R.string.DescribeStatus_battery_label);

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

  private static void addTelephonyStatus (StringBuilder sb) {
    TelephonyManager tel = (TelephonyManager)ApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);

    if (tel != null) {
      startLine(sb, R.string.DescribeStatus_sim_label);
      int state = tel.getSimState();

      switch (state) {
        default:
          Log.w(LOG_TAG, "unknown SIM state: " + state);
        case TelephonyManager.SIM_STATE_UNKNOWN:
          appendString(sb, R.string.DescribeStatus_sim_state_unknown);
          break;

        case TelephonyManager.SIM_STATE_ABSENT:
          appendString(sb, R.string.DescribeStatus_sim_state_absent);
          break;

        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
          appendString(sb, R.string.DescribeStatus_sim_state_locked_net);
          break;

        case TelephonyManager.SIM_STATE_PIN_REQUIRED:
          appendString(sb, R.string.DescribeStatus_sim_state_locked_pin);
          break;

        case TelephonyManager.SIM_STATE_PUK_REQUIRED:
          appendString(sb, R.string.DescribeStatus_sim_state_locked_puk);
          break;

        case TelephonyManager.SIM_STATE_READY: {
          String operator = tel.getSimOperatorName();

          if ((operator != null) && !operator.isEmpty()) {
            sb.append(operator);
          } else {
            appendString(sb, R.string.DescribeStatus_sim_state_ready);
          }

          break;
        }
      }
    }
  }

  private static void addWifiStatus (StringBuilder sb) {
    WifiManager wifi = (WifiManager)ApplicationContext.getSystemService(Context.WIFI_SERVICE);

    if (wifi != null) {
      startLine(sb, R.string.DescribeStatus_wifi_label);
      int state = wifi.getWifiState();

      switch (state) {
        default:
          Log.w(LOG_TAG, "unknown Wi-Fi state: " + state);
        case WifiManager.WIFI_STATE_UNKNOWN:
          appendString(sb, R.string.DescribeStatus_wifi_state_unknown);
          break;

        case WifiManager.WIFI_STATE_DISABLING:
          appendString(sb, R.string.DescribeStatus_wifi_state_disabling);
          break;

        case WifiManager.WIFI_STATE_DISABLED:
          appendString(sb, R.string.DescribeStatus_wifi_state_disabled);
          break;

        case WifiManager.WIFI_STATE_ENABLING:
          appendString(sb, R.string.DescribeStatus_wifi_state_enabling);
          break;

        case WifiManager.WIFI_STATE_ENABLED: {
          WifiInfo info = wifi.getConnectionInfo();

          if (info != null) {
            String name = info.getSSID();

            if ((name != null) && !name.isEmpty()) {
              sb.append(name);

              {
                int dbm = info.getRssi();

                sb.append(' ');
                sb.append(wifi.calculateSignalLevel(dbm, 100));
                sb.append('%');
              }

              {
                int speed = info.getLinkSpeed();

                if (speed > 0) {
                  sb.append(' ');
                  sb.append(speed);
                  sb.append("Mbps");
                }
              }
            } else {
              info = null;
            }
          }

          if (info == null) {
            appendString(sb, R.string.DescribeStatus_wifi_state_enabled);
          }

          break;
        }
      }
    }
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();

    addBatteryStatus(sb);
    addTelephonyStatus(sb);
    addWifiStatus(sb);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeStatus (Endpoint endpoint) {
    super(endpoint, false);
  }
}
