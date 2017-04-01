package org.nbp.b2g.ui;

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

  public final int getPercentFull () {
    if (haveBattery()) {
      int scale = batteryExtras.getInt(BatteryManager.EXTRA_SCALE, 0);

      if (scale > 0) {
        int level = batteryExtras.getInt(BatteryManager.EXTRA_LEVEL, 0);
        return (level * 100) / scale;
      }
    }

    return -1;
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
}
