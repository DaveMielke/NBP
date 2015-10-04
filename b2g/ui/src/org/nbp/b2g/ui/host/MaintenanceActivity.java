package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.io.File;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.AsyncTask;

import java.io.IOException;
import java.security.GeneralSecurityException;
import android.content.ActivityNotFoundException;

public class MaintenanceActivity extends ProgrammaticActivity {
  private final static String LOG_TAG = MaintenanceActivity.class.getName();

  private Context getContext () {
    return MaintenanceActivity.this;
  }

  private PowerManager getPowerManager () {
    return (PowerManager)getSystemService(Context.POWER_SERVICE);
  }

  private TextView messageView;

  private void setMessage (String message) {
    Devices.braille.get().write(message);
    messageView.setText(message);
    Log.d(LOG_TAG, "system maintenance: " + message);
  }

  private void setMessage (int message) {
    setMessage(getString(message));
  }

  private String getRebootFailureMessage () {
    return getString(R.string.maintenance_message_reboot_failed);
  }

  private void updateSystem (File file) {
    new AsyncTask<File, Integer, String>() {
      String progressMessage;
      File systemUpdate;

      @Override
      protected void onPreExecute () {
        progressMessage = getContext().getString(R.string.maintenance_UpdateSystem_verifying);
        setMessage(progressMessage);
      }

      @Override
      protected String doInBackground (File... files) {
        systemUpdate = files[0];
        String result = getRebootFailureMessage();

        RecoverySystem.ProgressListener progressListener = new RecoverySystem.ProgressListener() {
          @Override
          public void onProgress (int percentage) {
            publishProgress(percentage);
          }
        };

        try {
          RecoverySystem.verifyPackage(systemUpdate, progressListener, null);
          result = null;
        } catch (IOException exception) {
          result = "system update not readable: " + exception.getMessage();
        } catch (GeneralSecurityException exception) {
          result = "invalid system update: " + exception.getMessage();
        }

        return result;
      }

      @Override
      protected void onProgressUpdate (Integer... values) {
        int percentage = values[0];
        setMessage(String.format("%s: %d%%", progressMessage, percentage));
      }

      @Override
      protected void onPostExecute (String result) {
        if (result == null) {
          setMessage(R.string.maintenance_UpdateSystem_applying);
          String failure = getRebootFailureMessage();

          try {
            RecoverySystem.installPackage(getContext(), systemUpdate);
          } catch (IOException exception) {
            failure = exception.getMessage();
          }

          setMessage(failure);
        } else {
          setMessage(result);
        }
      }
    }.execute(file);
  }

  private void updateSystem (String path) {
    updateSystem(new File(path));
  }

  private void updateSystem (Uri uri) {
    updateSystem(uri.getPath());
  }

  private void updateSystem (Intent intent) {
    updateSystem(intent.getData());
  }

  private enum ActivityRequestType {
    FIND_SYSTEM_UPDATE;
  }

  @Override
  protected void onActivityResult (int requestCode, int resultCode, Intent resultData) {
    ActivityRequestType requestType = ActivityRequestType.values()[requestCode];

    if (resultCode == RESULT_OK) {
      switch (requestType) {
        case FIND_SYSTEM_UPDATE:
          updateSystem(resultData);
          break;
      }
    }
  }

  private void findFile (ActivityRequestType requestType) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("file/*");

    try {
      startActivityForResult(intent, requestType.ordinal());
    } catch (ActivityNotFoundException exception) {
      Log.w(LOG_TAG, "file system browser not found: " + exception.getMessage());
    }
  }

  private void rebootDevice (String reason) {
    getPowerManager().reboot(reason);
    setMessage(getRebootFailureMessage());
  }

  private View createRestartSystemButton () {
    Button button = newButton(
      R.string.maintenance_RestartSystem_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_RestartSystem_starting);
          rebootDevice(null);
        }
      }
    );

    return button;
  }

  private View createRecoveryModeButton () {
    Button button = newButton(
      R.string.maintenance_RecoveryMode_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_RecoveryMode_starting);
          rebootDevice("recovery");
        }
      }
    );

    return button;
  }

  private View createBootLoaderButton () {
    Button button = newButton(
      R.string.maintenance_BootLoader_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_BootLoader_starting);
          rebootDevice("bootloader");
        }
      }
    );

    return button;
  }

  private View createUpdateSystemButton () {
    Button button = newButton(
      R.string.maintenance_UpdateSystem_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_UpdateSystem_finding);
          findFile(ActivityRequestType.FIND_SYSTEM_UPDATE);
        }
      }
    );

    return button;
  }

  private View createClearCacheButton () {
    Button button = newButton(
      R.string.maintenance_ClearCache_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_ClearCache_starting);
          String failure = getRebootFailureMessage();

          try {
            RecoverySystem.rebootWipeCache(getContext());
          } catch (IOException exception) {
            failure = exception.getMessage();
          }

          setMessage(failure);
        }
      }
    );

    return button;
  }

  private View createFactoryResetButton () {
    Button button = newButton(
      R.string.maintenance_FactoryReset_label,
      new Button.OnClickListener() {
        @Override
        public void onClick (View view) {
          setMessage(R.string.maintenance_FactoryReset_starting);
          String failure = getRebootFailureMessage();

          try {
            RecoverySystem.rebootWipeUserData(getContext());
          } catch (IOException exception) {
            failure = exception.getMessage();
          }

          setMessage(failure);
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

    parameters.leftMargin = getLeftMargin();

    messageView = newTextView();
    view.addView(messageView);

    view.addView(createRestartSystemButton());
    view.addView(createRecoveryModeButton());
    view.addView(createBootLoaderButton());

    view.addView(createUpdateSystemButton());
    view.addView(createClearCacheButton());
    view.addView(createFactoryResetButton());

    return view;
  }
}
