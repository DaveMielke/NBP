package org.nbp.navigator;

import org.nbp.common.CommonActivity;
import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.util.Log;
import android.os.Bundle;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

public class NavigatorActivity extends CommonActivity {
  private final static String LOG_TAG = NavigatorActivity.class.getName();

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

  private static NavigatorActivity navigatorActivity = null;

  public final static NavigatorActivity getNavigatorActivity () {
    return navigatorActivity;
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navigatorActivity = this;

    setContentView(R.layout.navigator);
    Controls.restore();

    getFragmentManager()
      .beginTransaction()
      .add(R.id.navigator_fragment_container, new NavigationFragment())
      .commit();
  }

  @Override
  protected void onDestroy () {
    try {
      navigatorActivity = null;
    } finally {
      super.onDestroy();
    }
  }
}
