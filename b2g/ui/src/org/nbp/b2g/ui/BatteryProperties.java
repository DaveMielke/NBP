package org.nbp.b2g.ui;

import org.nbp.common.Timeout;
import android.util.Log;

import android.os.Bundle;
import android.os.BatteryManager;
import android.content.Intent;

public class BatteryProperties extends SystemProperties {
  private final Bundle batteryExtras;

  public BatteryProperties () {
    super("class/power_supply/battery-0");
    batteryExtras = HostMonitor.getIntentExtras(Intent.ACTION_BATTERY_CHANGED);
  }

  public final Bundle getExtras () {
    return batteryExtras;
  }

  public final boolean haveBattery () {
    return batteryExtras.getBoolean(BatteryManager.EXTRA_PRESENT, true);
  }

  public final Double getPercentFull () {
    int scale = batteryExtras.getInt(BatteryManager.EXTRA_SCALE, 0);

    if (scale > 0) {
      int level = batteryExtras.getInt(BatteryManager.EXTRA_LEVEL, -1);

      if (level >= 0) {
        return (double)(level * 100) / (double)scale;
      }
    }

    return null;
  }

  public final Double getTemperature () {
    int temperature = batteryExtras.getInt(BatteryManager.EXTRA_TEMPERATURE, -1);
    if (temperature < 0) return null;
    return (double)temperature / 10d;
  }

  public final Double getVoltage () {
    int voltage = batteryExtras.getInt(BatteryManager.EXTRA_VOLTAGE, -1);
    if (voltage < 0) return null;

    if (voltage < 100) voltage *= 1000;
    return (double)voltage / 1000d;
  }

  public final Double getCurrent () {
    int current = getIntegerProperty("current_now", -1);
    if (current < 0) return null;
    return (double)current * 1.0E-6d;
  }

  private final static Timeout batteryReportInterval = new Timeout(ApplicationParameters.BATTERY_REPORT_INTERVAL, "battery-report-interval") {
    @Override
    public void run () {
      batteryReport();
    }
  };

  private final static void append (StringBuilder sb, Double value, int precision, String unit, double multiplier) {
    if (value != null) {
      if (sb.length() > 0) sb.append(' ');
      sb.append(String.format(("%." + precision + "f"), (value * multiplier)));
      sb.append(unit);
    }
  }

  private final static void append (StringBuilder sb, Double value, int precision, String unit) {
    append(sb, value, precision, unit, 1d);
  }

  private final static void batteryReport () {
    BatteryProperties battery = new BatteryProperties();

    if (battery.haveBattery()) {
      StringBuilder log = new StringBuilder();
      append(log, battery.getPercentFull(), 0, "%");
      append(log, battery.getCurrent(), 0, "mA", 1E3);
      append(log, battery.getVoltage(), 2, "V");
      append(log, battery.getTemperature(), 1, "°C");
      if (log.length() > 0) Log.i("battery-report", log.toString());
    }

    batteryReportInterval.start();
  }

  static {
    batteryReport();
  }
}
