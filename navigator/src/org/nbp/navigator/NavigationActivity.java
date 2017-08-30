package org.nbp.navigator;

import org.nbp.navigator.controls.ActivationLevelControl;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.util.Log;
import android.os.Bundle;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

public class NavigationActivity extends BaseActivity {
  private final static String LOG_TAG = NavigationActivity.class.getName();

  private final void menuAction_settings () {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  private final void menuAction_about () {
    DialogFinisher finisher = new DialogFinisher() {
      @Override
      public void finishDialog (DialogHelper helper) {
        helper.setText(R.id.about_version_number, R.string.NBP_Navigator_version_name);
        helper.setText(R.id.about_build_time, R.string.NBP_Navigator_build_time);
        helper.setText(R.id.about_source_revision, R.string.NBP_Navigator_source_revision);
        helper.setTextFromAsset(R.id.about_copyright, "copyright");
      }
    };

    showDialog(R.string.menu_about, R.layout.about, finisher);
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    int identifier = item.getItemId();

    switch (identifier) {
      case R.id.menu_settings:
        menuAction_settings();
        return true;

      case R.id.menu_about:
        menuAction_about();
        return true;
    }

    String name = getResources().getResourceEntryName(identifier);
    if (name == null) name = Integer.toString(identifier);
    Log.w(LOG_TAG, ("unhandled menu action: " + name));
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  private static NavigationActivity navigationActivity = null;
  private OrientationMonitor orientationMonitor = null;

  public final static NavigationActivity getNavigationActivity () {
    return navigationActivity;
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigationActivity = this;

    setContentView(R.layout.navigation);
    finishBaseActivityCreation();

    orientationMonitor = new OrientationMonitor();
    Controls.restore();
  }

  @Override
  protected void onDestroy () {
    try {
      orientationMonitor = null;
      navigationActivity = null;
    } finally {
      super.onDestroy();
    }
  }

  private final static ActivationLevelControl[] CONTROLS = new ActivationLevelControl[] {
    Controls.locationMonitor
  };

  @Override
  protected void onResume () {
    super.onResume();
    for (ActivationLevelControl control : CONTROLS) control.onResume();
    orientationMonitor.start();
  }

  @Override
  protected void onPause () {
    try {
      orientationMonitor.stop();
      for (ActivationLevelControl control : CONTROLS) control.onPause();
    } finally {
      super.onPause();
    }
  }
}
