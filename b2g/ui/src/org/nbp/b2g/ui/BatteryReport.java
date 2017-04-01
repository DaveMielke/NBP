package org.nbp.b2g.ui;

import org.nbp.common.Timeout;
import android.util.Log;

public abstract class BatteryReport {
  private BatteryReport () {
  }

  private final static Timeout reportInterval = new Timeout(ApplicationParameters.BATTERY_REPORT_INTERVAL, "battery-report-interval") {
    @Override
    public void run () {
      report();
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

  private final static void report () {
    BatteryProperties battery = new BatteryProperties();

    if (battery.haveBattery()) {
      StringBuilder log = new StringBuilder();
      append(log, battery.getPercentFull(), 0, "%");
      append(log, battery.getCurrent(), 0, "mA", 1E3);
      append(log, battery.getVoltage(), 2, "V");
      append(log, battery.getTemperature(), 1, "°C");
      if (log.length() > 0) Log.i("battery-report", log.toString());
    }

    reportInterval.start();
  }

  public final static void start () {
    report();
  }
}
