package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.BooleanControl;

import android.content.Context;
import android.content.Intent;

public class AlertMonitorControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_AlertMonitor;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "alert-monitor";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.ALERT_MONITOR;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.ALERT_MONITOR;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.ALERT_MONITOR = value;

    Context context = ApplicationComponent.getContext();
    Intent intent = AlertService.makeIntent(context);

    if (value) {
      context.startService(intent);
    } else {
      context.stopService(intent);
    }

    return true;
  }

  public AlertMonitorControl () {
    super();
  }
}
