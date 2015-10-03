package org.nbp.b2g.ui;

import java.io.IOException;

import android.util.Log;
import android.os.Bundle;

import android.os.RecoverySystem;

import android.content.Context;
import android.os.PowerManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;

public class MaintenanceActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = MaintenanceActivity.class.getName();

  public PowerManager getPowerManager () {
    return (PowerManager)getSystemService(Context.POWER_SERVICE);
  }
  private void rebootDevice (String reason) {
    getPowerManager().reboot(reason);
  }

  private View createRebootAndroidButton () {
    Button button = createButton(
      R.string.maintenance_RebootAndroid_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          rebootDevice(null);
        }
      }
    );

    return button;
  }

  private View createRecoveryModeButton () {
    Button button = createButton(
      R.string.maintenance_RecoveryMode_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          rebootDevice("recovery");
        }
      }
    );

    return button;
  }

  private View createBootLoaderButton () {
    Button button = createButton(
      R.string.maintenance_BootLoader_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          rebootDevice("bootloader");
        }
      }
    );

    return button;
  }

  private View createWipeCacheButton () {
    Button button = createButton(
      R.string.maintenance_WipeCache_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          try {
            RecoverySystem.rebootWipeCache(MaintenanceActivity.this);
          } catch (IOException exception) {
          }
        }
      }
    );

    return button;
  }

  private View createWipeCacheAndDataButton () {
    Button button = createButton(
      R.string.maintenance_WipeCacheAndData_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          try {
            RecoverySystem.rebootWipeUserData(MaintenanceActivity.this);
          } catch (IOException exception) {
          }
        }
      }
    );

    return button;
  }

  @Override
  protected final View createContentView () {
    LinearLayout view = new LinearLayout(this);
    view.setOrientation(view.VERTICAL);

    LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    );

    parameters.leftMargin = ApplicationContext.dipsToPixels(
      ApplicationParameters.SCREEN_LEFT_OFFSET
    );

    view.addView(createRebootAndroidButton());
    view.addView(createRecoveryModeButton());
    view.addView(createBootLoaderButton());

    view.addView(createWipeCacheButton());
    view.addView(createWipeCacheAndDataButton());

    return view;
  }

  @Override
  public void onCreate (Bundle state) {
    super.onCreate(state);
    ApplicationContext.setContext(this);
    setContentView();
  }
}
