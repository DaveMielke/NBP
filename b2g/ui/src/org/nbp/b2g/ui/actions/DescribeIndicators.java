package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.Context;
import android.os.Bundle;
import android.os.Build;

import android.os.BatteryManager;

import android.telephony.TelephonyManager;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;

public class DescribeIndicators extends Action {
  private final static String LOG_TAG = DescribeIndicators.class.getName();

  private static void appendString (StringBuilder sb, int resource) {
    String string = ApplicationContext.getString(resource);

    if (!string.isEmpty()) {
      sb.append(' ');
      sb.append(string);
    }
  }

  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
    sb.append(":");
  }

  private class IndicatorProperty {
    private final Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    public final void report (StringBuilder sb, int value) {
      Integer text = map.get(value);
      if (text != null) appendString(sb, text);
    }

    protected final void addValue (int value, int text) {
      map.put(value, text);
    }
  }

  private final IndicatorProperty batteryHealth = new IndicatorProperty() {
    {
      addValue(BatteryManager.BATTERY_HEALTH_GOOD, R.string.DescribeIndicators_battery_health_good);
      addValue(BatteryManager.BATTERY_HEALTH_DEAD, R.string.DescribeIndicators_battery_health_dead);
      addValue(BatteryManager.BATTERY_HEALTH_COLD, R.string.DescribeIndicators_battery_health_cold);
      addValue(BatteryManager.BATTERY_HEALTH_OVERHEAT, R.string.DescribeIndicators_battery_health_hot);
      addValue(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, R.string.DescribeIndicators_battery_health_voltage);
      addValue(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE, R.string.DescribeIndicators_battery_health_failure);
    }
  };

  private final IndicatorProperty batteryPlugged = new IndicatorProperty() {
    {
      addValue(BatteryManager.BATTERY_PLUGGED_AC, R.string.DescribeIndicators_battery_plugged_ac);
      addValue(BatteryManager.BATTERY_PLUGGED_USB, R.string.DescribeIndicators_battery_plugged_usb);

      if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
        addValue(BatteryManager.BATTERY_PLUGGED_WIRELESS, R.string.DescribeIndicators_battery_plugged_wireless);
      }
    }
  };

  private final IndicatorProperty batteryStatus = new IndicatorProperty() {
    {
      addValue(BatteryManager.BATTERY_STATUS_CHARGING, R.string.DescribeIndicators_battery_status_charging);
      addValue(BatteryManager.BATTERY_STATUS_DISCHARGING, R.string.DescribeIndicators_battery_status_discharging);
      addValue(BatteryManager.BATTERY_STATUS_FULL, R.string.DescribeIndicators_battery_status_full);
      addValue(BatteryManager.BATTERY_STATUS_NOT_CHARGING, R.string.DescribeIndicators_battery_status_not_charging);
    }
  };

  private void reportBatteryIndicators (StringBuilder sb) {
    Bundle battery = HostMonitor.getBatteryStatus();

    if (battery != null) {
      startLine(sb, R.string.DescribeIndicators_battery_label);

      {
        sb.append(' ');
        boolean present = battery.getBoolean(BatteryManager.EXTRA_PRESENT, true);

        if (present) {
          int level = battery.getInt(BatteryManager.EXTRA_LEVEL, 0);
          int scale = battery.getInt(BatteryManager.EXTRA_SCALE, 0);

          if (scale > 0) {
            sb.append(Integer.toString((level * 100) / scale));
            sb.append('%');
          }
        } else {
          sb.append("none");
        }
      }

      batteryStatus.report(sb, battery.getInt(BatteryManager.EXTRA_STATUS, 0));
      batteryPlugged.report(sb, battery.getInt(BatteryManager.EXTRA_STATUS, 0));
      batteryHealth.report(sb, battery.getInt(BatteryManager.EXTRA_HEALTH, 0));
    }
  }

  private final IndicatorProperty simState = new IndicatorProperty() {
    {
      addValue(TelephonyManager.SIM_STATE_UNKNOWN, R.string.DescribeIndicators_sim_state_unknown);
      addValue(TelephonyManager.SIM_STATE_ABSENT, R.string.DescribeIndicators_sim_state_absent);
      addValue(TelephonyManager.SIM_STATE_NETWORK_LOCKED, R.string.DescribeIndicators_sim_state_locked_net);
      addValue(TelephonyManager.SIM_STATE_PIN_REQUIRED, R.string.DescribeIndicators_sim_state_locked_pin);
      addValue(TelephonyManager.SIM_STATE_PUK_REQUIRED, R.string.DescribeIndicators_sim_state_locked_puk);
      addValue(TelephonyManager.SIM_STATE_READY, R.string.DescribeIndicators_sim_state_ready);
    }
  };

  private void reportTelephonyIndicators (StringBuilder sb) {
    TelephonyManager tel = (TelephonyManager)ApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);

    if (tel != null) {
      startLine(sb, R.string.DescribeIndicators_sim_label);
      int state = tel.getSimState();

      switch (state) {
        case TelephonyManager.SIM_STATE_READY: {
          String operator = tel.getSimOperatorName();

          if ((operator != null) && !operator.isEmpty()) {
            sb.append(' ');
            sb.append(operator);
            break;
          }
        }

        default:
          simState.report(sb, state);
          break;
      }
    }
  }

  private final IndicatorProperty wifiState = new IndicatorProperty() {
    {
      addValue(WifiManager.WIFI_STATE_UNKNOWN, R.string.DescribeIndicators_wifi_state_unknown);
      addValue(WifiManager.WIFI_STATE_DISABLING, R.string.DescribeIndicators_wifi_state_disabling);
      addValue(WifiManager.WIFI_STATE_DISABLED, R.string.DescribeIndicators_wifi_state_disabled);
      addValue(WifiManager.WIFI_STATE_ENABLING, R.string.DescribeIndicators_wifi_state_enabling);
      addValue(WifiManager.WIFI_STATE_ENABLED, R.string.DescribeIndicators_wifi_state_enabled);
    }
  };

  private void reportWifiIndicators (StringBuilder sb) {
    WifiManager wifi = (WifiManager)ApplicationContext.getSystemService(Context.WIFI_SERVICE);

    if (wifi != null) {
      startLine(sb, R.string.DescribeIndicators_wifi_label);
      int state = wifi.getWifiState();

      switch (state) {
        case WifiManager.WIFI_STATE_ENABLED: {
          WifiInfo info = wifi.getConnectionInfo();

          if (info != null) {
            String name = info.getSSID();

            if ((name != null) && !name.isEmpty()) {
              sb.append(' ');
              sb.append(name);

              {
                int rssi = info.getRssi();

                sb.append(' ');
                sb.append(wifi.calculateSignalLevel(rssi, 101));
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

              break;
            }
          }
        }

        default:
          wifiState.report(sb, state);
          break;
      }
    }
  }

  @Override
  public boolean performAction () {
    StringBuilder sb = new StringBuilder();

    reportBatteryIndicators(sb);
    reportTelephonyIndicators(sb);
    reportWifiIndicators(sb);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeIndicators (Endpoint endpoint) {
    super(endpoint, false);
  }
}
