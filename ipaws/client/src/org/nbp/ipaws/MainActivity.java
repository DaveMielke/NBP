package org.nbp.ipaws;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Switch;

public class MainActivity extends Activity {
  private final static String LOG_TAG = MainActivity.class.getName();

  private Switch mainSwitch = null;

  private final Intent makeAlertServiceIntent () {
    return new Intent(this, AlertService.class);
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);
    mainSwitch = (Switch)findViewById(R.id.main_switch);
  }

  @Override
  protected void onResume () {
    super.onResume();
    mainSwitch.setChecked((AlertService.getAlertService() != null));
  }

  public final void mainSwitchToggled (View view) {
    boolean isOn = mainSwitch.isChecked();

    if (isOn) {
      startService(makeAlertServiceIntent());
    } else {
      stopService(makeAlertServiceIntent());
    }
  }
}
