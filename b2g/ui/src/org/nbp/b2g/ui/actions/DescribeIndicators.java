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
  protected final static String LOG_TAG = DescribeIndicators.class.getName();

  private static void appendString (StringBuilder sb, String string) {
    sb.append(string);
  }

  private static void appendString (StringBuilder sb, int string) {
    appendString(sb, ApplicationContext.getString(string));
  }

  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
    sb.append(":");
  }

  private class IndicatorProperty {
    private final String logLabel;

    private final Map<Integer, String> map = new HashMap<Integer, String>();

    public final boolean report (StringBuilder sb, int value) {
      if (ApplicationSettings.LOG_ACTIONS) {
        Log.v(LOG_TAG, (logLabel + ": " + value));
      }

      String text = map.get(value);
      if (text == null) return false;

      sb.append(' ');
      appendString(sb, text);
      return true;
    }

    protected final void addValue (int value, String text) {
      map.put(value, text);
    }

    protected final void addValue (int value, int text) {
      map.put(value, ApplicationContext.getString(text));
    }

    public IndicatorProperty (String label) {
      logLabel = label;
    }
  }

  private final IndicatorProperty batteryHealth = new IndicatorProperty("battery health") {
    {
      addValue(BatteryManager.BATTERY_HEALTH_COLD, R.string.DescribeIndicators_battery_health_cold);
      addValue(BatteryManager.BATTERY_HEALTH_DEAD, R.string.DescribeIndicators_battery_health_dead);
      addValue(BatteryManager.BATTERY_HEALTH_GOOD, R.string.DescribeIndicators_battery_health_good);
      addValue(BatteryManager.BATTERY_HEALTH_OVERHEAT, R.string.DescribeIndicators_battery_health_hot);
      addValue(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, R.string.DescribeIndicators_battery_health_voltage);
      addValue(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE, R.string.DescribeIndicators_battery_health_failure);
    }
  };

  private final IndicatorProperty batteryPlugged = new IndicatorProperty("battery plugged") {
    {
      addValue(BatteryManager.BATTERY_PLUGGED_AC, R.string.DescribeIndicators_battery_plugged_ac);
      addValue(BatteryManager.BATTERY_PLUGGED_USB, R.string.DescribeIndicators_battery_plugged_usb);

      if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
        addValue(BatteryManager.BATTERY_PLUGGED_WIRELESS, R.string.DescribeIndicators_battery_plugged_wireless);
      }
    }
  };

  private final IndicatorProperty batteryStatus = new IndicatorProperty("battery status") {
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
        if (HostMonitor.haveBattery(battery)) {
          int percentage = HostMonitor.getBatteryPercentage(battery);
          if (percentage >= 0) {
            sb.append(' ');
            sb.append(percentage);
            sb.append('%');
          }
        } else {
          sb.append(' ');
          appendString(sb, R.string.DescribeIndicators_battery_present_no);
        }
      }

      batteryStatus.report(sb, battery.getInt(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN));
      batteryPlugged.report(sb, battery.getInt(BatteryManager.EXTRA_PLUGGED, -1));
      batteryHealth.report(sb, battery.getInt(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN));

      if (ApplicationSettings.DEVELOPER_ENABLED) {
        {
          int voltage = battery.getInt(BatteryManager.EXTRA_VOLTAGE, -1);

          if (voltage >= 0) {
            sb.append(' ');

            if (voltage < 100) {
              sb.append(voltage);
            } else {
              sb.append((float)voltage / 1000f);
            }

            sb.append('V');
          }
        }

        {
          int temperature = battery.getInt(BatteryManager.EXTRA_TEMPERATURE, -1);

          if (temperature >= 0) {
            sb.append(' ');
            sb.append((float)temperature / 10f);
            sb.append('°');
            sb.append('C');
          }
        }
      }
    }
  }

  private final IndicatorProperty simState = new IndicatorProperty("SIM state") {
    {
      addValue(TelephonyManager.SIM_STATE_UNKNOWN, R.string.DescribeIndicators_sim_state_unknown);
      addValue(TelephonyManager.SIM_STATE_ABSENT, R.string.DescribeIndicators_sim_state_absent);
      addValue(TelephonyManager.SIM_STATE_NETWORK_LOCKED, R.string.DescribeIndicators_sim_state_locked_net);
      addValue(TelephonyManager.SIM_STATE_PIN_REQUIRED, R.string.DescribeIndicators_sim_state_locked_pin);
      addValue(TelephonyManager.SIM_STATE_PUK_REQUIRED, R.string.DescribeIndicators_sim_state_locked_puk);
      addValue(TelephonyManager.SIM_STATE_READY, R.string.DescribeIndicators_sim_state_ready);
    }
  };

  private final IndicatorProperty phoneType = new IndicatorProperty("phone type") {
    {
      addValue(TelephonyManager.PHONE_TYPE_CDMA, "CDMA");
      addValue(TelephonyManager.PHONE_TYPE_GSM, "GSM");
      addValue(TelephonyManager.PHONE_TYPE_SIP, "SIP");
    }
  };

  private final IndicatorProperty callState = new IndicatorProperty("call state") {
    {
      addValue(TelephonyManager.CALL_STATE_IDLE, R.string.DescribeIndicators_sim_call_idle);
      addValue(TelephonyManager.CALL_STATE_OFFHOOK, R.string.DescribeIndicators_sim_call_offhook);
      addValue(TelephonyManager.CALL_STATE_RINGING, R.string.DescribeIndicators_sim_call_ringing);
    }
  };

  private final IndicatorProperty networkType = new IndicatorProperty("call state") {
    {
      addValue(TelephonyManager.NETWORK_TYPE_1xRTT, "1xRTT");
      addValue(TelephonyManager.NETWORK_TYPE_CDMA, "CDMA");
      addValue(TelephonyManager.NETWORK_TYPE_EDGE, "EDGE");
      addValue(TelephonyManager.NETWORK_TYPE_EHRPD, "EHRPD");
      addValue(TelephonyManager.NETWORK_TYPE_EVDO_0, "EVDO(0)");
      addValue(TelephonyManager.NETWORK_TYPE_EVDO_A, "EVDO(A)");
      addValue(TelephonyManager.NETWORK_TYPE_EVDO_B, "EVDO(B)");
      addValue(TelephonyManager.NETWORK_TYPE_GPRS, "GPRS");
      addValue(TelephonyManager.NETWORK_TYPE_HSDPA, "HSDPA");
      addValue(TelephonyManager.NETWORK_TYPE_HSPA, "HSPA");
      addValue(TelephonyManager.NETWORK_TYPE_HSPAP, "HSPA+");
      addValue(TelephonyManager.NETWORK_TYPE_HSUPA, "HSUPA");
      addValue(TelephonyManager.NETWORK_TYPE_IDEN, "IDEN");
      addValue(TelephonyManager.NETWORK_TYPE_LTE, "LTE");
      addValue(TelephonyManager.NETWORK_TYPE_UMTS, "UMTS");
    }
  };

  private final IndicatorProperty dataState = new IndicatorProperty("call state") {
    {
      addValue(TelephonyManager.DATA_CONNECTED, R.string.DescribeIndicators_sim_data_connected);
      addValue(TelephonyManager.DATA_CONNECTING, R.string.DescribeIndicators_sim_data_connecting);
      addValue(TelephonyManager.DATA_DISCONNECTED, R.string.DescribeIndicators_sim_data_disconnected);
      addValue(TelephonyManager.DATA_SUSPENDED, R.string.DescribeIndicators_sim_data_suspended);
    }
  };

  private void reportTelephonyIndicators (StringBuilder sb) {
    TelephonyManager tel = (TelephonyManager)ApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);

    if (tel != null) {
      startLine(sb, R.string.DescribeIndicators_sim_label);
      int state = tel.getSimState();

      switch (state) {
        case TelephonyManager.SIM_STATE_READY: {
          String operator = tel.getNetworkOperatorName();

          if ((operator != null) && !operator.isEmpty()) {
            sb.append(' ');
            sb.append(operator);

            sb.append(' ');
            sb.append(PhoneMonitor.getSignalStrength());
            sb.append("dBm");

            phoneType.report(sb, tel.getPhoneType());
            callState.report(sb, tel.getCallState());

            if (networkType.report(sb, tel.getDataState())) {
              dataState.report(sb, tel.getDataState());
            }

            break;
          }
        }

        default:
          simState.report(sb, state);
          break;
      }
    }
  }

  private final IndicatorProperty wifiState = new IndicatorProperty("Wi-Fi state") {
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
